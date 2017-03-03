package com.jontian.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import java.util.Arrays;

@Configuration
public class Config {
    /*
        NOTE: FetchType.LAZY won't work because Jackson will try to run getter.
        Thanks chrismarx http://stackoverflow.com/questions/21708339/avoid-jackson-serialization-on-non-fetched-lazy-objects
        Add this bean solves this issue.
     */
    @Bean
    public Module datatypeHibernateModule() {
        return new Hibernate5Module();
    }
}
