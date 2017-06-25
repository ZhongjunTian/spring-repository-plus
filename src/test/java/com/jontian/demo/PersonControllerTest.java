package com.jontian.demo;
import com.jontian.demo.db.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = {Application.class})
public class PersonControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(PersonControllerTest.class);

    @Autowired
    TestRestTemplate restTemplate;
    @Test
    public void httpGetWithFilterTest(){
        String queryString = "lastName~eq~Tian~and~address.city~eq~Dallas";
        ResponseEntity<Person[]> resp = restTemplate.getForEntity("/persons?filter=" + queryString, Person[].class);
        Person[] persons = resp.getBody();
        Assert.assertTrue(persons.length>0);
    }

    @Test
    public void filter() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String simpleFilter = "{\n" +
                "\"field\":\"lastName\", \"operator\":\"eq\", \"value\":\"Tian\"\n" +
                "}";
        logger.info("Simple Filter:");
        logger.info(simpleFilter);
        ResponseEntity<String> resp = restTemplate.postForEntity("/persons", new HttpEntity<>(simpleFilter,headers), String.class);
        String persons = resp.getBody();
        Assert.assertEquals(resp.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(persons.length()>0);
        String complexFilter = "{\n" +
                "\"logic\" : \"and\",\n" +
                "\"filters\":[\n" +
                "{\n" +
                "\"field\":\"lastName\", \"operator\":\"eq\", \"value\":\"Tian\"\n" +
                "},\n" +
                "{\n" +
                "\"logic\" : \"or\",\n" +
                "\"filters\":[\n" +
                "{ \n" +
                "\"field\":\"address.city\", \"operator\":\"eq\",\"value\":\"Dallas\"\n" +
                "},\n" +
                "{\n" +
                "\"field\":\"address.city\", \"operator\":\"eq\",\"value\":\"San Francisco\"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "]\n" +
                "}";
        logger.info("Complex Filter:");
        logger.info(complexFilter);
        resp = restTemplate.postForEntity("/persons", new HttpEntity<>(complexFilter,headers), String.class);
        persons = resp.getBody();
        Assert.assertEquals(resp.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(persons.length()>0);
    }

}