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

import com.jontian.specification.exception.SpecificationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jon (Zhongjun Tian)
 */
public class Filter {
    /*
    logic
     */
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String DELIMITER = "~";
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
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";

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
            //xxx~eq~yyy
            return field + DELIMITER + operator + DELIMITER + value;
        } else if (filters != null && !filters.isEmpty()) {
            //(xxx~eq~yyy~and~aaa~eq~bbb)
            return LEFT_BRACKET +String.join(DELIMITER+logic+DELIMITER,
                    filters.stream().map(Filter::toString).collect(Collectors.toList()))+ RIGHT_BRACKET;
        } else {
            return "";
        }
    }

    public static Filter parse(String queryString){
        if(queryString == null || queryString.isEmpty())
            return null;
        if(queryString.startsWith(LEFT_BRACKET) && queryString.endsWith(RIGHT_BRACKET)){
            queryString = queryString.substring(1,queryString.length()-1);
        }
        if(queryString.contains(LEFT_BRACKET) && queryString.contains(RIGHT_BRACKET)){
            //TODO
        }else{
            String[] params = queryString.split(DELIMITER);
            if(params.length%4 == 3){
                String logic = null;
                for(int i=3; i<params.length; i+=4){
                    if(logic == null){
                        logic = params[i];
                    }
                    if(!params[i].equalsIgnoreCase(logic)){
                        throw new SpecificationException("Illegal query string with two different logic: "+logic+", "+params[i]);
                    }
                }
                if(logic==null){
                    return new Filter(params[0], params[1],params[2]);
                }else if(logic.equalsIgnoreCase(AND) || logic.equalsIgnoreCase(OR)){
                    int n = (params.length+1)/4;
                    Filter[] filters = new Filter[n];
                    for(int i=0; i<n*4; i+=4){
                        filters[i/4] = new Filter(params[i], params[i+1], params[i+2]);
                    }
                    if(logic.equalsIgnoreCase(AND)){
                        return and(filters);
                    }else{
                        return or(filters);
                    }
                }else{
                    throw new SpecificationException("Unknown logic: "+logic);
                }

            }else{
                throw new SpecificationException("Illegal query string, format: "+queryString);
            }
        }
        return null;
    }
}
