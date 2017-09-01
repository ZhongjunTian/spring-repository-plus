package io.metro.specification;

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

public class SpecificationImpl implements Specification<Object> {
    private static final Logger logger = LoggerFactory.getLogger(SpecificationImpl.class);
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
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    public void add(Specification<Object> specification){
        specifications.add(specification);
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }
}

