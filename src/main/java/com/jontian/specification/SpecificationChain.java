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
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *@author Jon (Zhongjun Tian)
 */
public class SpecificationChain implements Specification<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SpecificationChain.class);
    private List<Specification> specifications = new LinkedList<>();


    /*
     this method is called by
     SimpleJpaRepository.applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query)
     https://github.com/spring-projects/spring-data-jpa/blob/master/src/main/java/org/springframework/data/jpa/repository/support/SimpleJpaRepository.java
     */
    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        for(Specification specification: specifications){
            Predicate p = specification.toPredicate(root, cq, cb);
            if(p!=null)
                predicates.add(p);
        }
        Predicate[] predicateArray = predicates.toArray(new Predicate[predicates.size()]);
        return cb.and(predicateArray);
    }

    public void add(Specification<Object> specification){
        specifications.add(specification);
    }

}

