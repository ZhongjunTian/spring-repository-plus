package com.jontian.demo.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zhongjun on 5/4/17.
 */

public interface TestEntityRepository extends JpaRepository<TestEntity, Long>,JpaSpecificationExecutor {
}
