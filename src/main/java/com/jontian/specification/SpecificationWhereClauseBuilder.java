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


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class SpecificationWhereClauseBuilder<T> {
    private SpecificationBuilder specificationBuilder;
    private String path;
    private SpecificationWhereClauseBuilder(){};

    public <T> SpecificationWhereClauseBuilder(String path, SpecificationBuilder<T> specificationBuilder) {
        this.specificationBuilder = specificationBuilder;
        this.path = path;
    }

    public SpecificationWhereClauseBuilder equal(Object obj){
        specificationBuilder.where(new Filter(path, Filter.EQUAL, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder notEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.NOT_EQUAL, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder contains(Object obj){
        specificationBuilder.where(new Filter(path, Filter.CONTAINS, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder notContains(Object obj){
        specificationBuilder.where(new Filter(path, Filter.NOT_CONTAINS, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder greaterThan(Object obj){
        specificationBuilder.where(new Filter(path, Filter.GREATER_THAN, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder lessThan(Object obj){
        specificationBuilder.where(new Filter(path, Filter.LESS_THAN, obj));
        return this;
    }
    
    public SpecificationWhereClauseBuilder greaterThanOrEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.GREATER_THAN_OR_EQUAL, obj));
        return this;
    }

    public SpecificationWhereClauseBuilder lessThanOrEqual(Object obj){
        specificationBuilder.where(new Filter(path, Filter.LESS_THAN_OR_EQUAL, obj));
        return this;
    }

    public List<T> findAll() {
        return specificationBuilder.findAll();
    }

    public Page<T> findPage(Pageable page) {
        return specificationBuilder.findPage(page);
    }

}
