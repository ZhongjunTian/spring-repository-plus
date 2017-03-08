package com.jontian.specification;

import java.util.Arrays;

/**
 * Created by zhongjun on 3/7/17.
 */
public class FilterBuilder {
    public static Filter and(Filter ...filters) {
        Filter filter = new Filter();
        filter.setLogic(Filter.AND);
        filter.setFilters(Arrays.asList(filters));
        return filter;
    }
    public static Filter or(Filter ... filters){
        Filter filter = new Filter();
        filter.setLogic(Filter.OR);
        filter.setFilters(Arrays.asList(filters));
        return filter;
    }
}
