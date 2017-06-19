package com.jontian.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * Created by zhongjun on 6/18/17.
 */
public class JoinSpecification implements Specification<Object> {
    String[] leftJoinFetchTables;

    public JoinSpecification(String[] tables) {
        leftJoinFetchTables = tables;
    }

    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        leftJoinFetchTables(root,cq,leftJoinFetchTables);
        return null;
    }


    private void leftJoinFetchTables(Root<Object> root, CriteriaQuery<?> cq, String[] joinFetchTables) {
        //because this piece of code may be run twice for pagination,
        //first time 'count' , second time 'select',
        //So, if this is called by 'count', don't join fetch tables.
        if (isCountCriteriaQuery(cq))
            return;
        if( joinFetchTables != null && (joinFetchTables.length > 0)) {
            for (String table : joinFetchTables) {
                if(table != null)
                    root.fetch(table, JoinType.LEFT);
            }
            ((CriteriaQuery<Object>) cq).select(root);
        }
    }


    /*
        For Issue:
        when run repository.findAll(specs,page)
        The method toPredicate(...) upon will return a Predicate for Count(TableName) number of rows.
        In hibernate query, we cannot do "select count(table_1) from table_1 left fetch join table_2 where ..."
        Resolution:
        In this scenario, CriteriaQuery<?> is CriteriaQuery<Long>, because return type is Long.
        we don't fetch other tables where generating query for "count";
     */
    private boolean isCountCriteriaQuery(CriteriaQuery<?> cq) {
        return cq.getResultType().toString().contains("java.lang.Long");
    }
}
