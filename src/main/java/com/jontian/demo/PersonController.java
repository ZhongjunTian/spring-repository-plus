package com.jontian.demo;

import com.fasterxml.jackson.annotation.JsonView;
import com.jontian.demo.db.Person;
import com.jontian.demo.db.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.core.EmbeddedWrapper;
//import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jontian.specification.SpecificationBuilder.select;
import static com.jontian.specification.Filter.*;

@RestController
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @GetMapping("/")
    public List<Person> smipleFilter(){
        List<Person> persons = select(Person.class).from(personRepository).where("lastName", EQUAL, "Tian").findAll();
        return persons;
    }

    @GetMapping("/join")
    public List<Person> joinFetch(){
        return select(Person.class).from(personRepository).leftJoin("address").where("lastName", EQUAL, "Tian").findAll();
    }

    @GetMapping("/oldWay")
    public List<Person> oldSchool(){
        return personRepository.findByLastName("Tian");
    }
}
