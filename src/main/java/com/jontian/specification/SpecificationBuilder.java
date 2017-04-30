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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 *@author Jon (Zhongjun Tian)
 */
public class SpecificationBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(SpecificationBuilder.class);

    private JpaSpecificationExecutor repository;
    private List<String> leftJoinTable;
    //TODO private List<String> innerJoinTable;
    //TODO private List<String> rightJoinTable;
    private Filter filter;

    public static <ENTITY, REPO extends JpaRepository<ENTITY,?> & JpaSpecificationExecutor>
    SpecificationBuilder<ENTITY> selectFrom(REPO repository){
        SpecificationBuilder<ENTITY> builder = new SpecificationBuilder<>();
        builder.repository = repository;
        builder.leftJoinTable = new ArrayList<>();
        return builder;
    }


    public SpecificationBuilder<T> leftJoin(String... tables){
        if(tables!=null)
            for(String table : tables){
                leftJoinTable.add(table);
            }
        return this;
    }



    public SpecificationBuilder<T> where(String field, String operator, Object value){
        return where(new Filter(field, operator, value));
    }

    public SpecificationBuilder<T> where(Filter filter){
        if(this.repository == null){
            throw new IllegalStateException("Did not specify which repository, please use from() before where()");
        }
        if(this.filter != null){
            throw new IllegalStateException("Cannot use where() twice");
        }
        this.filter = filter;
        return this;
    }


    public <R> SpecificationWhereClauseBuilder where(String field){
        return new SpecificationWhereClauseBuilder(field, this);
    }

    public List<T> findAll(){
        return repository.findAll(new SpecificationImpl(filter, leftJoinTable));
    }

    public Page<T> findPage(Pageable page){
        return repository.findAll(new SpecificationImpl(filter, leftJoinTable), page);
    }

}
