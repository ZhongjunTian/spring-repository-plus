package com.jontian.demo.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by zhongjun on 5/4/17.
 */
@Entity
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    //data type for test
    public java.util.Date utilDate;
    public java.sql.Date sqlDate;
    public BigDecimal bigDecimal;
    public String string;

    public Character aChar;
    public Long aLong;
    public Integer aInteger;
    public Double aDouble;
    public Float aFloat;
    public Boolean aBoolean;
}
