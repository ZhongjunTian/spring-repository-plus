package com.jontian.demo;

import com.jontian.demo.db.Person;
import com.jontian.demo.db.PersonRepository;
import com.jontian.specification.Filter;
import com.jontian.specification.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.core.EmbeddedWrapper;
//import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class PersonController {
    @Autowired
    PersonRepository personRepository;
    @GetMapping("/")
    public ResponseEntity<List<Person>> smipleFilter(){
        Filter filter = new Filter("lastName", Filter.EQUAL, "Tian");
        GenericSpecification specification = new GenericSpecification(filter);
        List<Person> persons = personRepository.findAll(specification);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/join")
    public ResponseEntity<List<Person>> joinFetch(){
        Filter filter = new Filter("lastName", Filter.EQUAL, "Tian");
        String joinFetchTable = "address";
        GenericSpecification specification = new GenericSpecification(filter, joinFetchTable);
        List<Person> persons = personRepository.findAll(specification);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/oldWay")
    public ResponseEntity<List<Person>> oldSchool(){
        List<Person> persons = personRepository.findByLastName("Tian");
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

//    public PagedResources<?> toPagedResources(Page<?> data, Class<?> clazz) {
//        List<?> content = data.getContent();
//
//        if (content.isEmpty()) {
//            EmbeddedWrapper wrapper = new EmbeddedWrappers(false).emptyCollectionOf(clazz);
//            content = Collections.singletonList(wrapper);
//        }
//        return new PagedResources<>(content,
//                new PagedResources.PageMetadata(
//                        (long) data.getSize(), (long) data.getNumber(), (long) data.getTotalElements(), (long) data.getTotalPages())
//        );
//
//    }
}
