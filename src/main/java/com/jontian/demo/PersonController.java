package com.jontian.demo;

import com.jontian.demo.db.Person;
import com.jontian.demo.db.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.core.EmbeddedWrapper;
//import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.data.domain.Page;
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
    public List<Person> simple(){
        return select(Person.class).from(personRepository).findAll();
    }

    @GetMapping("/join")
    public List<Person> join(){
        return select(Person.class).from(personRepository).leftJoin("address").findAll();
    }

    @GetMapping("/page")
    public Page<Person> page(){
        return select(Person.class).from(personRepository).findPage(null);
    }

    @GetMapping("/lambda")
    public Page<Person> lambda() throws Exception {
        return select(Person.class)
                .from(personRepository)
                .where((person -> person.getAddress().getCity()), EQUAL, "Dallas")
                .findPage(null);
    }

    @GetMapping("/oldWay")
    public List<Person> oldWay(){
        return personRepository.findByLastName("Tian");
    }

}
