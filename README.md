
# spring-repository-plus

A easy way to extend Spring Boot and Hibernate to have high performance, high flexibility and JOOQ functional style code

Pros:

1. Programmally filter data in simple code,<br />

```JAVA
     List persons = selectFrom(personRepository).findAll();
     List persons = selectFrom(personRepository).where("lastName", EQUAL, "Tian").findAll();
     List persons = selectFrom(personRepository).leftJoin("address").findAll();
     Page persons = selectFrom(personRepository).findPage();
     List persons = selectFrom(personRepository)
          .where((person -> person.getAddress().getCity()), EQUAL, "Dallas")
         .findAll();
```
Querying with JOOQ is as simple as this : 

```JAVA
  Book b = BOOK.as("b");
        Author a = AUTHOR.as("a");
        BookStore s = BOOK_STORE.as("s");
        BookToBookStore t = BOOK_TO_BOOK_STORE.as("t");

        Result<Record3<String, String, Integer>> result =
        create.select(a.FIRST_NAME, a.LAST_NAME, countDistinct(s.NAME))
              .from(a)
              .join(b).on(b.AUTHOR_ID.equal(a.ID))
              .join(t).on(t.BOOK_ID.equal(b.ID))
              .join(s).on(t.BOOK_STORE_NAME.equal(s.NAME))
              .groupBy(a.FIRST_NAME, a.LAST_NAME)
              .fetch();
```
Querying with Querydsl JPA is as simple as this :

```JAVA
QCustomer customer = QCustomer.customer;
JPAQuery<?> query = new JPAQuery<Void>(entityManager);
Customer bob = query.select(customer)
  .from(customer)
  .where(customer.firstName.eq("Bob"))
  .fetchOne();
```
2. don't have to predefine ignoring findByXXXXXXXXXXXXXXXXXXXXXX() in Repository <br />
3. Join Fetch table without writing Hibernate HQL, which gives us high performance<br />

<h2>User guide</h2>
<h3>1. clone and run</h3>

git clone https://github.com/ZhongjunTian/spring-repository-plus.git <br />
cd spring-repository-plus<br />
mvn spring-boot:run

<h3>2. open example url</h3>

<h4>2.1 simple example</h4><br />
open url <br />
<a href="http://localhost:8080/"><b>http://localhost:8080/</b></a><br />

Then look back at application's console, Hibernate will print out executed query

<h4>2.2 advanced example</h4><br />
open url <br />
<a href="http://localhost:8080/join"><b>http://localhost:8080/join</b></a><br />
