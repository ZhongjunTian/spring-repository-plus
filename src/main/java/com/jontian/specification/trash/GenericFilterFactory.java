package com.jontian.specification.trash;

import com.jontian.specification.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtian on 11/1/2016.
 */


class GenericFilterFactory {
    protected static final String EQUAL = "eq";
    protected static final String NOT_EQUAL = "neq";
    protected static final String EMPTY_OR_NULL = "isnull";
    protected static final String NOT_EMPTY_AND_NOT_NULL = "isnotnull";

    protected static final String CONTAINS = "contains";
    protected static final String NOT_CONTAINS = "doesnotcontain";
    protected static final String START_WITH = "startswith";
    protected static final String END_WITH = "endswith";

    protected static final String GREATER_THAN = "gt";
    protected static final String LESS_THAN = "lt";
    protected static final String GREATER_THAN_OR_EQUAL = "gte";
    protected static final String LESS_THAN_OR_EQUAL = "lte";
    protected static final String PATH_DELIMITER = ".";

    protected static final String[] BASE_OPERATOR_COLLECTION = {"{", "}"};
    protected static final String AND_DELIMITER = ",";

    protected static List<Filter> createGeneralFilters(String expressions) {
        List<Filter> filters = new ArrayList<>();
        for (String expression : expressions.split(AND_DELIMITER)) {
            if (expression.isEmpty())
                continue;
            int i = firstOperatorIndex(expression);
            int j = lastOperatorIndex(expression);
            String field = expression.substring(0, i);
            String operator = expression.substring(i + 1, j);
            String value = expression.substring(j + 1, expression.length());
            filters.add(new Filter(field, operator, value));
        }
        return filters;
    }

    private static int firstOperatorIndex(String expression) {
        int m = -1;
        for (String operator : BASE_OPERATOR_COLLECTION) {
            int n = expression.indexOf(operator);
            if (m == -1 || n != -1 && n < m)
                m = n;
        }
        if (m == -1)
            throw new RuntimeException("Missing operator " + BASE_OPERATOR_COLLECTION);
        return m;
    }

    private static int lastOperatorIndex(String expression) {
        int m = -1;
        for (String operator : BASE_OPERATOR_COLLECTION) {
            int n = expression.lastIndexOf(operator);
            m = Math.max(m, n);
        }
        if (m == -1)
            throw new RuntimeException("Missing operator " + BASE_OPERATOR_COLLECTION);
        return m;
    }

}