package com.jontian.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhongjun on 5/4/17.
 */
@Service
public class TestEntityService {
    @Autowired
    TestEntityRepository testEntityRepository;

    public void nothing(){
        testEntityRepository.findAll();
    }
}
