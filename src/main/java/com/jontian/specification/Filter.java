/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jontian.specification;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jon (Zhongjun Tian)
 */
public class Filter {
    /*
    logic
     */
    public static final String AND = "and";
    public static final String OR = "or";
    /*
    Operators
     */
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

    //delimiter for crossing table search
    public static final String PATH_DELIMITER = ".";

    String field;
    String operator;
    Object value;
    String logic;
    List<Filter> filters;

    public Filter() {
    }

    public Filter(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public Filter(String logic, Filter... filters) {
        this.logic = logic;
        this.filters = Arrays.asList(filters);
    }

    public Filter and(Filter filters) {
        return new Filter(AND, filters);
    }

    public Filter or(Filter filters) {
        return new Filter(OR, filters);
    }

    public static Filter and(Filter... filters) {
        return new Filter(AND, filters);
    }

    public static Filter or(Filter... filters) {
        return new Filter(OR, filters);
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
    public String toString() {
        if (logic == null) {
            return "{" + field + " " + operator + " " + value + "}";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (filters != null)
                filters.forEach(f -> sb.append(f.toString()));
            sb.append("}");
            return sb.toString();
        }
    }
}
