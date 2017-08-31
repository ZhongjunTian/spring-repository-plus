package io.metro.specification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;

public class SpecificationBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(SpecificationBuilder.class);

    private JpaSpecificationExecutor repository;
    private SpecificationImpl specification;

    public static <ENTITY, REPO extends JpaRepository<ENTITY, ?> & JpaSpecificationExecutor>
    SpecificationBuilder<ENTITY> selectFrom(REPO repository) {
        SpecificationBuilder<ENTITY> builder = new SpecificationBuilder<>();
        builder.repository = repository;
        builder.specification = new SpecificationImpl();
        return builder;
    }

    public static <ENTITY, REPO extends JpaRepository<ENTITY, ?> & JpaSpecificationExecutor>
    SpecificationBuilder<ENTITY> selectDistinctFrom(REPO repository) {
        SpecificationBuilder<ENTITY> builder = selectFrom(repository);
        builder.distinct();
        return builder;
    }

    private SpecificationBuilder distinct() {
        specification.add(((root, criteriaQuery, criteriaBuilder) -> criteriaQuery.distinct(true).getRestriction()));
        return this;
    }

    public SpecificationBuilder<T> where(String queryString) {
        Filter filter = Filter.parse(queryString);
        return where(filter);
    }

    public SpecificationBuilder<T> where(Filter filter) {
        if (this.repository == null) {
            throw new IllegalStateException("Did not specify which repository, please use from() before where()");
        }
        specification.add(new WhereSpecification(filter));
        return this;
    }

    public SpecificationBuilder<T> leftJoin(String... tables) {
        specification.add(new JoinSpecification().setLeftJoinFetchTables(Arrays.asList(tables)));
        return this;
    }

    public SpecificationBuilder<T> innerJoin(String... tables) {
        specification.add(new JoinSpecification().setInnerJoinFetchTables(Arrays.asList(tables)));
        return this;
    }

    public SpecificationBuilder<T> rightJoin(String... tables) {
        specification.add(new JoinSpecification().setRightJoinFetchTables(Arrays.asList(tables)));
        return this;
    }

    public List<T> findAll() {
        return repository.findAll(specification);
    }

    public Page<T> findPage(Pageable page) {
        return repository.findAll(specification, page);
    }

}
