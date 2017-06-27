package com.jontian.specification;

import com.jontian.specification.exception.SpecificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jontian.specification.Filter.*;

/**
 * Created by zhongjun on 6/18/17.
 */
public class WhereSpecification implements Specification<Object> {
    private static Logger logger = LoggerFactory.getLogger(WhereSpecification.class);
    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Filter filter;

    public WhereSpecification(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) throws SpecificationException {
        return getPredicate(filter, root, cb);
    }

    private Predicate getPredicate(Filter filter, Path<Object> root, CriteriaBuilder cb) throws SpecificationException {
        if (isInValidFilter(filter))
            return null;
        if (filter.getLogic() == null) {//one filter
            Predicate p = doGetPredicate(filter, root, cb);
            return p;
        } else {//logic filters
            if (filter.getLogic().equals(AND)) {
                Predicate[] predicates = getPredicateList(filter, root, cb);
                return cb.and(predicates);
            } else if (filter.getLogic().equals(OR)) {
                Predicate[] predicates = getPredicateList(filter, root, cb);
                return cb.or(predicates);
            } else {
                throw new SpecificationException("Unknown filter logic" + filter.getLogic());
            }
        }
    }

    private Predicate[] getPredicateList(Filter filter, Path<Object> root, CriteriaBuilder cb) throws SpecificationException {
        List<Predicate> predicateList = new LinkedList<>();
        for (Filter f : filter.getFilters()) {
            Predicate predicate = getPredicate(f, root, cb);
            if (predicate != null)
                predicateList.add(predicate);
        }
        return predicateList.toArray(new Predicate[predicateList.size()]);
    }


    private boolean isInValidFilter(Filter filter) {
        return filter == null ||
                (filter.getField() == null && filter.getFilters() == null && filter.getLogic() == null && filter.getValue() == null && filter.getOperator() == null);
    }

    private Predicate doGetPredicate(Filter filter, Path<Object> root, CriteriaBuilder cb) throws SpecificationException {
        String field = filter.getField();
        Path<Object> path = null;
        try {
            path = parsePath(root, field);
        } catch (Exception e) {
            throw new SpecificationException("Meet problem when parse field path: " + field + ", this path does not exist. " + e.getMessage(), e);
        }
        String operator = filter.getOperator();
        Object value = filter.getValue();
        try {
            return doGetPredicate(cb, path, operator, value);
        } catch (Exception e) {
            throw new SpecificationException("Unable to filter by: " + String.valueOf(filter) + ", value type:" + value.getClass() + ", operator: " + operator + ", entity type:" + path.getJavaType() + ", message: " + e.getMessage(), e);
        }
    }

    private Predicate doGetPredicate(CriteriaBuilder cb, Path<Object> path, String operator, Object value) throws SpecificationException {
        Class<? extends Object> entityType = path.getJavaType();
        Predicate p = null;
        //look at Hibernate Mapping types
        //we only support primitive types and data/time types
        if (!(value instanceof Comparable)) {
            throw new IllegalStateException("This library only support primitive types and date/time types in the list: " +
                    "Integer, Long, Double, Float, Short, BidDecimal, Character, String, Byte, Boolean" +
                    ", Date, Time, TimeStamp, Calendar");
        }
        switch (operator) {
            /*
                Operator for Comparable type
             */
            case EQUAL:
                value = parseValue(path, value);
                p = cb.equal(path, (value));
                break;
            case NOT_EQUAL:
                value = parseValue(path, value);
                p = cb.notEqual(path, (value));
                break;
            /*
                Operator for any type
             */
            case IS_NULL:
                p = cb.isNull(path);
                break;
            case IS_NOT_NULL:
                p = cb.isNotNull(path);
                break;
            /*
                Operator for String type
             */
            case IS_EMPTY:
                p = cb.equal(path,"");
                break;
            case IS_NOT_EMPTY:
                p = cb.notEqual(path,"");
                break;
            case CONTAINS:
                p = cb.like(path.as(String.class), "%" + String.valueOf(value) + "%");
                break;
            case NOT_CONTAINS:
                p = cb.notLike(path.as(String.class), "%" + String.valueOf(value) + "%");
                break;
            case START_WITH:
                p = cb.like(path.as(String.class), String.valueOf(value) + "%");
                break;
            case END_WITH:
                p = cb.like(path.as(String.class), "%" + String.valueOf(value));
                break;
            /*
                Operator for Comparable type;
                does not support Calendar
             */
            case GREATER_THAN:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.greaterThan(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.greaterThan(path.as(String.class), (value).toString());
                }
                break;
            case GREATER_THAN_OR_EQUAL:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.greaterThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.greaterThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.lessThan(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.lessThan(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN_OR_EQUAL:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.lessThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else {
                    p = cb.lessThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            /*
                Functionality in experimenting;
             */
            case IN:
                if (assertCollection(value)) {
                    p = path.in((Collection) value);
                }
                break;
            default:
                logger.error("unknown operator: " + operator);
                throw new IllegalStateException("unknown operator: " + operator);
        }
        return p;
    }

    private Object parseValue(Path<Object> path, Object value) {
        if (Date.class.isAssignableFrom(path.getJavaType())) {
            try {
                value = defaultDateFormat.parse(value.toString());
            } catch (ParseException e) {
                throw new SpecificationException("Illegal date format: " + value + ", required format is " + defaultDateFormat.toPattern());
            }
        }
        return value;
    }

    private boolean assertCollection(Object value) {
        if (value instanceof Collection) {
            return true;
        }
        throw new IllegalStateException("After operator " + IN + " should be a list, not '" + value + "'");
    }

    private Path<Object> parsePath(Path<? extends Object> root, String field) {
        if (!field.contains(PATH_DELIMITER)) {
            return root.get(field);
        }
        int i = field.indexOf(PATH_DELIMITER);
        String firstPart = field.substring(0, i);
        String secondPart = field.substring(i + 1, field.length());
        Path<Object> p = root.get(firstPart);
        return parsePath(p, secondPart);
    }
}
