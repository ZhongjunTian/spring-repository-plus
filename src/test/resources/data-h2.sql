
insert into ADDRESS (city) values ('Austin');
insert into ADDRESS (city) values ('Dallas');
insert into ADDRESS (city) values ('Los Angeles');
insert into ADDRESS (city) values ('New York');
insert into ADDRESS (city) values ('Orlando');
insert into ADDRESS (city) values ('San Francisco');
insert into PERSON (first_name, last_name, address_id) values ('Jon', 'Tian',null);
insert into PERSON (first_name, last_name, address_id) values ('Jon_1', 'Tian',2);
insert into PERSON (first_name, last_name, address_id) values ('Jon_2', 'Tian',3);
insert into PERSON (first_name, last_name, address_id) values ('Jon_3', 'Tian',4);
insert into PERSON (first_name, last_name, address_id) values ('Jon_4', 'Tian',5);
insert into PERSON (first_name, last_name, address_id) values ('Jon_5', 'Tian',6);

insert into Test_Entity (
     util_date,
     sql_date,
     string,
     a_char,
     big_decimal,
     a_long,
     a_integer,
     a_double,
     a_float,
    a_boolean ) values
     (
     '2000-01-01 12:30:00','2000-01-01 12:30:00','a','a',
     1, 1, 1, 1, 1, true);