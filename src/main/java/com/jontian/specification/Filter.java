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
import java.util.LinkedList;
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
        return toSting(false);
    }

    public String toSting(boolean withBrackets) {
        if (logic == null) {
            //xxx~eq~yyy
            return field + DELIMITER + operator + DELIMITER + value;
        } else if (filters != null && !filters.isEmpty()) {
            //(xxx~eq~yyy~and~aaa~eq~bbb)
            String join = String.join(DELIMITER + logic + DELIMITER,
                    filters.stream().map(f -> f.toSting(true)).collect(Collectors.toList()));
            if (withBrackets)
                return LEFT_BRACKET + join + RIGHT_BRACKET;
            else
                return join;
        } else {
            return "";
        }
    }

    //(a~eq~a~and~(b~eq~b~or~b~eq~bb))
    public static Filter parse(String queryString) {
        if (queryString == null || queryString.isEmpty())
            return null;
        String[] params = queryString.split(DELIMITER);
        return parse(params, 0, params.length - 1);
    }

    private static Filter parse(String[] params, int s, int e) {
        int n = e - s + 1;
        if (n % 4 != 3)
            throw new IllegalStateException(String.join(DELIMITER, Arrays.asList(params).subList(s, e + 1)));
        if (params[s].startsWith(LEFT_BRACKET) && params[e].endsWith(RIGHT_BRACKET)) {
            params[s] = params[s].substring(1, params[s].length());
            params[e] = params[e].substring(0, params[e].length() - 1);
        }
        List<Filter> filters = new LinkedList<>();
        String logic = null;
        for (int i = s; i + 2 <= e; ) {
            if (params[i].startsWith(LEFT_BRACKET)) {
                int j = findRightBracket(params, i, e);
                Filter filter = parse(params, i, j);
                filters.add(filter);
                if (logic == null && j + 1 <= e) {
                    logic = params[j + 1];
                }
                i = j + 2;
            } else {
                if (logic == null && i + 3 <= e) {
                    logic = params[i + 3];
                }
                if (i + 3 <= e)
                    if (!params[i + 3].equals(logic) || !params[i + 3].equals(AND) && !params[i + 3].equals(OR))
                        throw new SpecificationException("Illegal logic or mixed logic in one level bracket");
                Filter filter = new Filter(params[i], params[i + 1], params[i + 2]);
                filters.add(filter);
                i += 4;
            }
        }
        if (logic == null) {
            return filters.get(0);
        } else if (logic.equals(OR)) {
            return or(filters.toArray(new Filter[filters.size()]));
        } else {
            return and(filters.toArray(new Filter[filters.size()]));
        }
    }

    private static int findRightBracket(String[] params, int i, int e) {
        int countLeft = 0;
        int countRight = 0;
        for (int j = i; j < e; j += 4) {
            if (params[j].startsWith(LEFT_BRACKET)) {
                for(int k=0; k<params[j].length(); k++){
                    if(params[j].substring(k,k+1).equals(LEFT_BRACKET)){
                        countLeft++;
                    }else{
                        break;
                    }
                }
            }
            if (params[j + 2].endsWith(RIGHT_BRACKET)) {
                for(int k=params[j + 2].length()-1; k>=0; k--){
                    String substring = params[j + 2].substring(k, k + 1);
                    if(substring.equals(RIGHT_BRACKET)){
                        countRight++;
                    }else{
                        break;
                    }
                }
            }
            if (countLeft == countRight) {
                return j + 2;
            }
        }
        throw new SpecificationException("");//TODO
    }
}
