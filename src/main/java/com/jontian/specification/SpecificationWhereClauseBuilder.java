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


public class SpecificationWhereClauseBuilder {
    private SpecificationBuilder specificationBuilder;
    private String path;
    private SpecificationWhereClauseBuilder(){};

    public <T> SpecificationWhereClauseBuilder(String path, SpecificationBuilder<T> specificationBuilder) {
        this.specificationBuilder = specificationBuilder;
        this.path = path;
    }

    public SpecificationBuilder equal(Object obj){
        specificationBuilder.where(new Filter(path, Filter.EQUAL, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder notEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.NOT_EQUAL, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder contains(Object obj){
        specificationBuilder.where(new Filter(path, Filter.CONTAINS, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder notContains(Object obj){
        specificationBuilder.where(new Filter(path, Filter.NOT_CONTAINS, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder greaterThan(Object obj){
        specificationBuilder.where(new Filter(path, Filter.GREATER_THAN, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder lessThan(Object obj){
        specificationBuilder.where(new Filter(path, Filter.LESS_THAN, obj));
        return specificationBuilder;
    }
    
    public SpecificationBuilder greaterThanOrEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.GREATER_THAN_OR_EQUAL, obj));
        return specificationBuilder;
    }

    public SpecificationBuilder lessThanOrEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.LESS_THAN_OR_EQUAL, obj));
        return specificationBuilder;
    }

}
