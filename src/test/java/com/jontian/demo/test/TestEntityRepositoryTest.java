package com.jontian.demo.test;

import com.jontian.specification.Filter;
import com.jontian.specification.SpecificationBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static com.jontian.specification.Filter.*;
import static com.jontian.specification.SpecificationBuilder.selectFrom;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class TestEntityRepositoryTest {

    @PersistenceContext
    EntityManager em;

    SimpleJpaRepository<TestEntity, Integer> repository;
    List entities;

    @Before
    public void setup(){
        JpaEntityInformation<TestEntity, Integer> information = new JpaMetamodelEntityInformation<>(
                TestEntity.class, em.getMetamodel());
        repository = new SimpleJpaRepository<>(information, em);
        entities = SpecificationBuilder.selectDistinctFrom(repository).where(new Filter("string",EQUAL,"a")).findAll();
        Assert.assertTrue(entities.size() >= 1);
    }

    @Test
    public void contextLoads() {
        testAll();
    }

    private void testAll() {
        try {
            entities = selectFrom(repository).where(new Filter("utilDate",GREATER_THAN,"2000-01-01 12:00:00")).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("string",EQUAL,("a"))).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aChar",EQUAL,'a')).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("bigDecimal",EQUAL,1)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("bigDecimal",EQUAL,1.)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("bigDecimal",EQUAL,BigDecimal.ONE)).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aLong",EQUAL,"1")).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aLong",EQUAL,1L)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aLong",EQUAL,1.)).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aInteger",EQUAL,"1")).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aInteger",EQUAL,1)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aInteger",EQUAL,1.)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aInteger",EQUAL,1L)).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aDouble",EQUAL,"1")).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aDouble",EQUAL,1.)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aDouble",EQUAL,1L)).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aFloat",EQUAL,"1")).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aFloat",EQUAL,1L)).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where(new Filter("aFloat",EQUAL,1.)).findAll();
            Assert.assertTrue(entities.size() >= 1);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

}
