package com.jontian.specification;

import org.junit.Assert;
import org.junit.Test;

import static com.jontian.specification.Filter.parse;

/**
 * Created by zhongjun on 6/24/17.
 */
public class FilterTest {
    @Test
    public void parseTest() throws Exception {

        String s;
        Filter filter;



        s = "a~eq~a";
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());

        s = "(a~eq~a)";
        filter = parse(s);
        Assert.assertEquals("a~eq~a", filter.toString());

        s = "a~eq~a~and~b~eq~b";
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());

        s = "a~eq~a~and~(b~eq~b)";
        filter = parse(s);
        Assert.assertEquals("a~eq~a~and~b~eq~b", filter.toString());

        s = "a~eq~a~and~(b~eq~b~or~c~eq~c)";
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());

        s = "a~eq~a~and~(b~eq~b~or~c~eq~c)~and~d~eq~d";
        parse(s);
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());


        s = "a~eq~a~and~((b~eq~b~or~c~eq~c)~and~d~eq~d)";
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());

        s = "a~eq~a~and~((b~eq~b~or~c~eq~c)~and~(d~eq~d))";
        filter = parse(s);
        Assert.assertEquals("a~eq~a~and~((b~eq~b~or~c~eq~c)~and~d~eq~d)", filter.toString());

        s = "a~eq~a~and~((b~eq~b~or~c~eq~c)~and~(d~eq~d~or~e~eq~e))";
        filter = parse(s);
        Assert.assertEquals(s, filter.toString());


    }

}