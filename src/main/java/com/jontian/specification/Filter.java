package com.jontian.specification;

import java.util.List;

public class Filter {
    public static final String EQUAL = "eq";
    public static final String NOT_EQUAL = "neq";
    public static final String EMPTY_OR_NULL = "isnull";
    public static final String NOT_EMPTY_AND_NOT_NULL = "isnotnull";

    public static final String CONTAINS = "contains";
    public static final String NOT_CONTAINS = "doesnotcontain";

    public static final String START_WITH = "startswith";
    public static final String END_WITH = "endswith";

    public static final String GREATER_THAN = "gt";
    public static final String LESS_THAN = "lt";
    public static final String GREATER_THAN_OR_EQUAL = "gte";
    public static final String LESS_THAN_OR_EQUAL = "lte";

    public static final String IN = "in";

    public static final String PATH_DELIMITER = ".";
    String field;
    String operator;
    Object value;
    public static final String LOGIC_AND ="and";
    public static final String LOGIC_OR ="or";
    String logic;
    List<Filter> filters;
    public Filter(){}
    public Filter(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public String toString(){
        //TODO to String
       return "";
    }
}
