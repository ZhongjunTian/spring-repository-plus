package com.jontian.specification;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Jon Tian on 2/25/17.
 */
public class SpecificationBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(SpecificationBuilder.class);

    public static <T, T2 extends JpaRepository<T,?> & JpaSpecificationExecutor > SpecificationBuilder<T>
        selectFrom(T2 repository){
        return new SpecificationBuilder<T>(repository);
    }
    private BytecodeParser bytecodeParser;
    private JpaSpecificationExecutor repository;
    private List<String> leftJoinTable;
    //TODO private List<String> innerJoinTable;
    //TODO private List<String> rightJoinTable;
    private Filter filter;
    private int lastState = 0;
    private static int LAST_STATE_FROM = 1;
    private static int LAST_STATE_JOIN = 2;
    private static int LAST_STATE_WHERE = 3;

    public SpecificationBuilder(JpaSpecificationExecutor repository){
        this.repository = repository;
        this.bytecodeParser = new BytecodeParser();
        leftJoinTable = new ArrayList<>();
    }

    public SpecificationBuilder<T> leftJoin(String... tables){
        if(tables!=null)
        for(String table : tables){
            leftJoinTable.add(table);
        }
        return this;
    }

    public SpecificationBuilder<T> leftJoin(Function<T,?> getter){
        String attributePath = bytecodeParser.getPathFromCallStack();
        return this.leftJoin(attributePath);
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

    public SpecificationBuilder<T> where(Function<T,?> getter, String operator, Object value) {
        String attributePath = bytecodeParser.getPathFromCallStack();
        where(new Filter(attributePath, operator,value));
        return this;
    }



    public List<T> findAll(){
        return repository.findAll(new SpecificationImpl(filter, leftJoinTable));
    }

    public Page<T> findPage(Pageable page){
        return repository.findAll(new SpecificationImpl(filter, leftJoinTable), page);
    }

    //TODO and and or Builder
    // <T extends Appendable & Closeable>
    public SpecificationBuilder and(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

    public SpecificationBuilder or(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

}
