package com.jontian.demo;

import com.jontian.demo.db.Person;
import com.jontian.demo.db.PersonRepository;
import com.jontian.specification.Filter;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.core.EmbeddedWrapper;
//import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jontian.specification.SpecificationBuilder.selectFrom;

@RestController
@CrossOrigin("*")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @PostMapping("/filter")
    public List<Person> filter(@RequestBody Filter filter){
        return selectFrom(personRepository).where(filter).findAll();
    }

    @GetMapping("/person")
    public List<Person> simple(){
        return selectFrom(personRepository).findAll();
    }

    @GetMapping("/join")
    public List<Person> join(){
        return selectFrom(personRepository).leftJoin("address").findAll();
    }

    @GetMapping("/page")
    public Page<Person> page(){
        return selectFrom(personRepository).findPage(null);
    }

    @GetMapping("/lambda")
    public List<Person> lambda() {
        return selectFrom(personRepository)
                .leftJoin("address")
                .where("address").equal("Dallas").findAll();
    }

    @GetMapping("/complexQuery")
    public List<Person> complexQuery() {
        return selectFrom(personRepository)
                .where("address").equal("Dallas")
                .findAll();
    }


}
