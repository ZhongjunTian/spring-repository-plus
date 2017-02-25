# Spring-Repository-Specification

A easy way to read Spring data repository using Specification

Pros:

1. don't have to write ignoring findBy*() in Repository <br />
2. Join Fetch table without writing Hibernate HQL<br />

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

<i>select person0_.id as id1_1_, person0_.address_id as address_2_1_, person0_.first_name as first_na3_1_, person0_.last_name as last_nam4_1_ from person person0_ where person0_.last_name=?<br />
select address0_.id as id1_0_0_, address0_.city as city2_0_0_ from address address0_ where address0_.id in (?, ?, ?, ?, ?)<i/>

<h4>2.2 advanced example</h4><br />
open url <br />
<a href="http://localhost:8080/join"><b>http://localhost:8080/join</b></a><br />

<i>select person0_.id as id1_1_0_, address1_.id as id1_0_1_, person0_.address_id as address_2_1_0_, person0_.first_name as first_na3_1_0_, person0_.last_name as last_nam4_1_0_, address1_.city as city2_0_1_ from person person0_ left outer join address address1_ on person0_.address_id=address1_.id where person0_.last_name=?<i/>
