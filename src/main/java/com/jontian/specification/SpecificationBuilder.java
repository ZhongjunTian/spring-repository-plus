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
    public static <T> SpecificationBuilder<T> select(Class<T> clazz){
        return new SpecificationBuilder<T>();
    }

    private JpaSpecificationExecutor repository;
    private List<String> leftJoinTable;
    //TODO private List<String> innerJoinTable;
    //TODO private List<String> rightJoinTable;
    private Filter filter;
    private int lastState = 0;
    private static int LAST_STATE_FROM = 1;
    private static int LAST_STATE_JOIN = 2;
    private static int LAST_STATE_WHERE = 3;

    public <T2 extends JpaSpecificationExecutor & JpaRepository<T,?>>
    SpecificationBuilder<T> from(T2 repository){
        this.repository = repository;
        leftJoinTable = new ArrayList<>();
        return this;
    }

    public SpecificationBuilder<T> leftJoin(String... tables){
        if(tables!=null)
        for(String table : tables){
            leftJoinTable.add(table);
        }
        return this;
    }

    public SpecificationBuilder<T> where(String field, String operator, Object value){
        if(repository == null){
            throw new IllegalStateException("Did not specify which repository, please use from() before where()");
        }
        if(filter != null){
            throw new IllegalStateException("Cannot use where() twice");
        }
        filter = new Filter(field, operator, value);
        return this;
    }

    public SpecificationBuilder<T> where(Function<T,?> getter, String operator, Object value) {
        StackTraceElement elementWhichCallsThisMethod = Thread.currentThread().getStackTrace()[2];
        int lineNumberInUpperLevelClass = elementWhichCallsThisMethod.getLineNumber();
        List<String> getterNames = Collections.emptyList();
        try {
            getterNames = BytecodeUtil.getMethodNameInLambdaFromBytecode(elementWhichCallsThisMethod.getClassName(),lineNumberInUpperLevelClass);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        logger.info("Found getter in lambda expression: "+getterNames.toString());
        String attributePath = toAttributePath(getterNames);
        where(attributePath, operator,value);
        return this;
    }

    private String toAttributePath(List<String> getterNames) {
        StringBuilder sb = new StringBuilder();
        for(String getterName : getterNames) {
            if (!getterName.startsWith("get")) {
                throw new IllegalStateException();
            }
            String attributeName = getterName.substring(3);
            attributeName = Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
            sb.append(Filter.PATH_DELIMITER+attributeName);
        }
        return sb.toString().substring(Filter.PATH_DELIMITER.length());
    }

    public List<T> findAll(){
        //TODO
        return repository.findAll(new GenericSpecification(filter, leftJoinTable));
    }

    public Page<T> findPage(Pageable page){
        //TODO
        return repository.findAll(new GenericSpecification(filter, leftJoinTable), page);
    }

    //TODO
    // <T extends Appendable & Closeable>
    public SpecificationBuilder and(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

    public SpecificationBuilder or(String field, String operator, Object value) {
        throw new NotImplementedException();
    }

    public Filter build() {
        throw new NotImplementedException();
    }


}
