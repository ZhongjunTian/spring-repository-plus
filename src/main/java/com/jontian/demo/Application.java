package com.jontian.demo;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

	/*
        NOTE: FetchType.LAZY won't work because Jackson will try to run getter.
        Thanks chrismarx http://stackoverflow.com/questions/21708339/avoid-jackson-serialization-on-non-fetched-lazy-objects
        Add this bean solves this issue.
     */
	@Bean
	public Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
