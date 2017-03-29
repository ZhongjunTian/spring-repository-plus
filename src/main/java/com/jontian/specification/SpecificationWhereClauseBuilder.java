package com.jontian.specification;


public class SpecificationWhereClauseBuilder {
    private SpecificationBuilder specificationBuilder;
    private String path;
    private SpecificationWhereClauseBuilder(){};
    public SpecificationWhereClauseBuilder(SpecificationBuilder specificationBuilder){
        this.specificationBuilder = specificationBuilder;
    }

    public <T> SpecificationWhereClauseBuilder(String path, SpecificationBuilder<T> specificationBuilder) {
        this.specificationBuilder = specificationBuilder;
        this.path = path;
    }

    public SpecificationBuilder equal(Object obj){
        specificationBuilder.where(path, Filter.EQUAL, obj);
        return specificationBuilder;
    }
}
