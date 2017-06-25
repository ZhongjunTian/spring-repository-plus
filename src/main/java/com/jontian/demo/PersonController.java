package com.jontian.demo;

import com.jontian.demo.db.Person;
import com.jontian.demo.db.PersonRepository;
import com.jontian.specification.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jontian.specification.SpecificationBuilder.selectFrom;

@RestController
@CrossOrigin("*")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @PostMapping("/persons")
    public List<Person> filter(@RequestBody Filter filter){
        return selectFrom(personRepository).leftJoin("address").where(filter).findAll();
    }

    @GetMapping("/persons")
    public List<Person> join(@RequestParam(value = "filter",required = false) String queryString){
        return selectFrom(personRepository).leftJoin("address").where(queryString).findAll();
    }


}
