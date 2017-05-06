package com.jontian.specification;

import com.jontian.demo.Application;
import com.jontian.demo.db.PersonRepository;
import com.jontian.demo.test.TestEntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static com.jontian.specification.SpecificationBuilder.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ApplicationTests {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    TestEntityRepository repository;
    List<List> allEntities = new LinkedList<List>() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (List list : this) {
                sb.append(list.size() + ",");
            }
            return sb.toString();
        }
    };
    List entities;

    @Test
    public void contextLoads() {
        testAll();
    }

    private void testAll() {
        try {
            entities = selectFrom(repository).where("utilDate").greaterThan("2000-01-01 12:00:00").findAll();
            entities = selectFrom(repository).where("utilDate").lessThan("2000-01-01 12:00:00").findAll();

            entities = SpecificationBuilder.selectFrom(repository).where("string").equal("a").findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aChar").equal('a').findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(1).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(1.).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(BigDecimal.ONE).findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal("1").findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(1L).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(1.).findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal("1").findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1.).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1L).findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal("1").findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1.).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
            assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal("1").findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
            assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1.).findAll();
            assertTrue(entities.size() >= 1);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

}
