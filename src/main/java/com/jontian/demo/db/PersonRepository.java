package com.jontian.demo.db;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long>,JpaSpecificationExecutor {

    List<Person> findByLastName(@Param("name") String name);
    Page<Person> findByLastName(@Param("name") String name, Pageable page);

}