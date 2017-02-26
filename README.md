# Spring-Repository-Specification

A easy way to read Spring data repository using Specification

Pros:

1. Programmally filter data in simple code,<br />
        Filter filter = new Filter("lastName", Filter.EQUAL, "Tian");<br />
        List<Person> persons = personRepository.findAll(new GenericSpecification(filter));<br />
2. don't have to predefine ignoring findByXXXXXXXXXXXXXXXXXXXXXX() in Repository <br />
3. Join Fetch table without writing Hibernate HQL, which gives us high performance<br />
 
 Supported operator:<br />
 	 EQUAL = "eq";<br />
   NOT_EQUAL = "neq";<br />
   EMPTY_OR_NULL = "isnull";<br />
   NOT_EMPTY_AND_NOT_NULL = "isnotnull";<br />
   CONTAINS = "contains";<br />
   NOT_CONTAINS = "doesnotcontain";<br />
   START_WITH = "startswith";<br />
   END_WITH = "endswith";<br />
   GREATER_THAN = "gt";<br />
   LESS_THAN = "lt";<br />
   GREATER_THAN_OR_EQUAL = "gte";<br />
   LESS_THAN_OR_EQUAL = "lte";<br />
   IN = "in";<br />
   
 Supported logic operator:<br />
 "and" "or"<br />
 
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

