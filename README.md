
# Spring-Repository-Specification

A easy way to extend Spring Boot and Hibernate to have high performance, high flexibility and JOOQ functional style code

Pros:

1. Programmally filter data in simple code,<br />

    List persons = selectFrom(personRepository).findAll();<br />
     
     List persons = selectFrom(personRepository).where("lastName", EQUAL, "Tian").findAll();<br />
     
     List persons = selectFrom(personRepository).leftJoin("address").findAll();<br />
     
     Page persons = selectFrom(personRepository).findPage();<br />
    
     List persons = selectFrom(personRepository)<br />
            &nbsp;&nbsp;&nbsp;&nbsp; .where((person -> person.getAddress().getCity()), EQUAL, "Dallas")<br />
             &nbsp;&nbsp;&nbsp;&nbsp;  .findAll();<br />
2. don't have to predefine ignoring findByXXXXXXXXXXXXXXXXXXXXXX() in Repository <br />
3. Join Fetch table without writing Hibernate HQL, which gives us high performance<br />

<h2>User guide</h2>
<h3>1. clone and run</h3>

git clone https://github.com/ZhongjunTian/Spring-Repository-Specification.git <br />
cd Spring-Repository-Specification<br />
mvn spring-boot:run

<h3>2. open example url</h3>

<h4>2.1 simple example</h4><br />
open url <br />
<a href="http://localhost:8080/"><b>http://localhost:8080/</b></a><br />

Then look back at application's console, Hibernate will print out executed query

<h4>2.2 advanced example</h4><br />
open url <br />
<a href="http://localhost:8080/join"><b>http://localhost:8080/join</b></a><br />
