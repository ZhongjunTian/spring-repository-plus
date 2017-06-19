package com.jontian.demo.test;

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
        entities = SpecificationBuilder.selectDistinctFrom(repository).where("string").equal("a").findAll();
        Assert.assertTrue(entities.size() >= 1);
    }

    @Test
    public void contextLoads() {
        testAll();
    }

    private void testAll() {
        try {
            entities = selectFrom(repository).where("utilDate").greaterThan("2000-01-01 12:00:00").findAll();
            entities = selectFrom(repository).where("utilDate").lessThan("2000-01-01 12:00:00").findAll();

            entities = SpecificationBuilder.selectFrom(repository).where("string").equal("a").findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aChar").equal('a').findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(1).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(1.).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(BigDecimal.ONE).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal("1").findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(1L).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(1.).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal("1").findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1.).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1L).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal("1").findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1.).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
            Assert.assertTrue(entities.size() >= 1);

            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal("1").findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
            Assert.assertTrue(entities.size() >= 1);
            entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1.).findAll();
            Assert.assertTrue(entities.size() >= 1);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

}
