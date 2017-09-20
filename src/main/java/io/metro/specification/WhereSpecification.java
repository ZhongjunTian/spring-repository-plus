package io.metro.specification;

import io.metro.specification.exception.SpecificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.metro.specification.Filter.*;

public class WhereSpecification implements Specification<Object> {
    private static Logger logger = LoggerFactory.getLogger(WhereSpecification.class);
    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat dateFormat;
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
                (filter.getField() == null && filter.getFilters() == null &&
                        filter.getLogic() == null && filter.getValue() == null && filter.getOperator() == null);
    }

    private Predicate doGetPredicate(Filter filter, Path<Object> root, CriteriaBuilder cb) throws SpecificationException {
        String field = filter.getField();
        Path<Object> path = null;
        try {
            path = parsePath(root, field);
        } catch (Exception e) {
            throw new SpecificationException("Exception occurred when parsing field path: " + field + ", this path does not exist. " + e.getMessage(), e);
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
        if (!(value instanceof Comparable) && !(value instanceof Collection)) {
            throw new IllegalStateException("This library only support primitive types and date/time types in the list: " +
                    "Integer, Long, Double, Float, Short, BidDecimal, Character, String, Byte, Boolean" +
                    ", Date, Time, TimeStamp, Calendar");
        }
        value = parseValue(path, value);
        switch (operator) {
            /*
                Operator for Comparable type
             */
            case EQUAL:
                p = cb.equal(path, (value));
                break;
            case NOT_EQUAL:
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
                if (value instanceof Date) {
                    p = cb.greaterThan(path.as(Date.class), (Date) (value));
                } else if (value instanceof Double) {
                    p = cb.greaterThan((Expression)path, (Double) (value));
                } else if (value instanceof Integer) {
                    p = cb.greaterThan((Expression)path, (Integer) (value));
                } else {
                    p = cb.greaterThan(path.as(String.class), (value).toString());
                }
                break;
            case GREATER_THAN_OR_EQUAL:
                if (value instanceof Date) {
                    p = cb.greaterThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else if (value instanceof Double) {
                    p = cb.greaterThanOrEqualTo((Expression)path, (Double) (value));
                } else if (value instanceof Integer) {
                    p = cb.greaterThanOrEqualTo((Expression)path, (Integer) (value));
                } else {
                    p = cb.greaterThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN:
                if (value instanceof Date) {
                    p = cb.lessThan(path.as(Date.class), (Date) (value));
                } else if (value instanceof Double) {
                    p = cb.lessThan((Expression)path, (Double) (value));
                } else if (value instanceof Integer) {
                    p = cb.lessThan((Expression)path, (Integer) (value));
                } else {
                    p = cb.lessThan(path.as(String.class), (value).toString());
                }
                break;
            case LESS_THAN_OR_EQUAL:
                value = parseValue(path, value);
                if (value instanceof Date) {
                    p = cb.lessThanOrEqualTo(path.as(Date.class), (Date) (value));
                } else if (value instanceof Double) {
                    p = cb.lessThanOrEqualTo((Expression)path, (Double)value);
                } else if (value instanceof Integer) {
                    p = cb.lessThanOrEqualTo((Expression)path, (Integer)value);
                } else {
                    p = cb.lessThanOrEqualTo(path.as(String.class), (value).toString());
                }
                break;
            /*
                Functionality in experimenting;
             */
            case BETWEEN:
                if (assertCollection(value)) {
                    List<Object> list = (List<Object>)value;
                    logger.error("{} Collection: {}", BETWEEN, list);
                    if (list.get(0) instanceof Date) {
                        Date item1 = (Date) list.get(0);
                        Date item2 = (Date) list.get(1);
                        logger.error("{}: {} and {}", BETWEEN, item1, item2);
                        p = cb.between(path.as(Date.class), item1, item2);

                    } else if (list.get(0) instanceof Double) {
                        Double item1 = (Double) list.get(0);
                        Double item2 = (Double) list.get(1);
                        logger.error("{}: {} and {}", BETWEEN, item1, item2);
                        p = cb.between((Expression)path, item1, item2);

                    } else if (list.get(0) instanceof Integer) {
                        Integer item1 = (Integer) list.get(0);
                        Integer item2 = (Integer) list.get(1);
                        logger.error("{}: {} and {}", BETWEEN, item1, item2);
                        p = cb.between((Expression)path, item1, item2);

                    } else {
                        String item1 = (String) list.get(0);
                        String item2 = (String) list.get(1);
                        logger.error("{}: {} and {}", BETWEEN, item1, item2);
                        p = cb.between(path.as(String.class), item1, item2);
                    }
                }
                break;
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
        logger.debug("{}: value=({}), type=({})", "parseValue", value, path.getJavaType());
        if (Date.class.isAssignableFrom(path.getJavaType())) {
            try {
                SimpleDateFormat dateFormat = this.dateFormat != null ? this.dateFormat : defaultDateFormat;
                return  dateFormat.parse(value.toString());
            } catch (ParseException e) {
                throw new SpecificationException("Illegal date format: " + value + ", required format is " + dateFormat.toPattern());
            }
        }

        if(Boolean.class.isAssignableFrom(path.getJavaType()) || Boolean.TYPE.isAssignableFrom(path.getJavaType()))
            return Boolean.parseBoolean(value.toString());

        if(Enum.class.isAssignableFrom(path.getJavaType())) {
            if(value instanceof Collection) {
                ((List) value).replaceAll(x -> parseValue(path, x));
                return value;
            }
            try {
                Class<?> clazz = Class.forName(path.getJavaType().getTypeName());
                Method method = clazz.getDeclaredMethod("valueOf", String.class);
                return method.invoke(clazz, value.toString());
            } catch (ClassNotFoundException e) {
                logger.error("ClassNotFoundException {}: value=({}), type=({}), class=({})", e.getMessage(), value,
                        path.getJavaType(), path.getJavaType().getCanonicalName());
                throw new SpecificationException(e);
            } catch (NoSuchMethodException e) {
                logger.error("NoSuchMethodException {}: value=({}), type=({}), class=({})", e.getMessage(), value,
                        path.getJavaType(), path.getJavaType().getCanonicalName());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                logger.error("IllegalAccessException {}: value=({}), type=({}), class=({})", e.getMessage(), value,
                        path.getJavaType(), path.getJavaType().getCanonicalName());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                logger.error("InvocationTargetException {}: value=({}), type=({}), class=({})", e.getMessage(), value,
                        path.getJavaType(), path.getJavaType().getCanonicalName());
                e.printStackTrace();
            }
        }

        if(Byte.class.isAssignableFrom(path.getJavaType()) || Byte.TYPE.isAssignableFrom(path.getJavaType()))
            return Byte.parseByte( value.toString() );

        if(Short.class.isAssignableFrom(path.getJavaType()) || Short.TYPE.isAssignableFrom(path.getJavaType()))
            return Short.parseShort( value.toString() );

        if(Integer.class.isAssignableFrom(path.getJavaType()) || Integer.TYPE.isAssignableFrom(path.getJavaType()))
            return Integer.parseInt( value.toString() );

        if(Long.class.isAssignableFrom(path.getJavaType()) || Long.TYPE.isAssignableFrom(path.getJavaType()))
            return Long.parseLong( value.toString() );

        if(Float.class.isAssignableFrom(path.getJavaType()) || Float.TYPE.isAssignableFrom(path.getJavaType()))
            return Float.parseFloat( value.toString() );

        if(Double.class.isAssignableFrom(path.getJavaType()) || Double.TYPE.isAssignableFrom(path.getJavaType()))
            return Double.parseDouble( value.toString() );

        return value;
    }

    private boolean assertCollection(Object value) {
        logger.debug("assertCollection: {}, {}", value.getClass().getSimpleName(), value);
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

    public static void setDefaultDateFormat(SimpleDateFormat defaultDateFormat) {
        WhereSpecification.defaultDateFormat = defaultDateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
