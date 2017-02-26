package com.jontian.specification;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Jon Tian on 2/25/17.
 */
public class FilterBuilder {
    Filter filter;

    //TODO
    //NOT TESTED
    public FilterBuilder() {
    }

    public FilterBuilder and(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

    public FilterBuilder or(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

    public Filter build() {
        throw new NotImplementedException();
    }

    public GenericSpecification toGenericSpecification() {
        throw new NotImplementedException();
    }


}
