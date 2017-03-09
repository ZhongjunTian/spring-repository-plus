
# spring-repository-plus

A easy way to extend Spring Boot and Hibernate

Pros:

DO NOT have to use maven to generate code, <br />
JUST copy 5 classes to your project, then you are ready to go <br />

Querying with spring-repository-plus is as simple as:

```JAVA
     List persons = selectFrom(personRepository).where("lastName", EQUAL, "Tian").findAll();
     List persons = selectFrom(personRepository).where((person -> person.getLastName()), EQUAL, "Tian") //refactory friendly feature :)
                   .findAll();
```
Comparing to querying with JOOQ is as simple as this : 

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
Comparing to querying with Querydsl JPA is as simple as this :

```JAVA
QCustomer customer = QCustomer.customer;
JPAQuery<?> query = new JPAQuery<Void>(entityManager);
Customer bob = query.select(customer)
  .from(customer)
  .where(customer.firstName.eq("Bob"))
  .fetchOne();
```

<h2>User guide</h2>
<h3>clone and run</h3>

git clone https://github.com/ZhongjunTian/spring-repository-plus.git <br />
cd spring-repository-plus<br />
mvn spring-boot:run


Other example code:
```Java
     List persons = selectFrom(personRepository).leftJoin("address").findAll();
     Page persons = selectFrom(personRepository).findPage(page);
```
