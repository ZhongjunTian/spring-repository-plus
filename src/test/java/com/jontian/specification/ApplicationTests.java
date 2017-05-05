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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ApplicationTests {

	@Autowired PersonRepository personRepository;
	@Autowired TestEntityRepository repository;
	List<List> allEntities = new LinkedList<List>(){
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			for(List list: this){
				sb.append(list.size()+",");
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
			entities = SpecificationBuilder.selectFrom(repository).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("string").equal("a").findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("aChar").equal('c').findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(0).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(0.).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("bigDecimal").equal(BigDecimal.ZERO).findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal("0").findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(0L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(0).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aLong").equal(0.).findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal("0").findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1.).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aInteger").equal(1L).findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aDouble").equal(1L).findAll();
			allEntities.add(entities);

			entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
			allEntities.add(entities);
			entities = SpecificationBuilder.selectFrom(repository).where("aFloat").equal(1L).findAll();
			allEntities.add(entities);
		}catch (Exception e){
			e.printStackTrace();
			e.getMessage();
		}
	}

}
