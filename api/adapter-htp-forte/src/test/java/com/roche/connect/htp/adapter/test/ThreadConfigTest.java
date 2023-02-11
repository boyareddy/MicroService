package com.roche.connect.htp.adapter.test;

import org.mockito.InjectMocks;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.roche.connect.htp.adapter.ThreadConfig;

 /*@TestPropertySource(properties = {"async.core-pool-size=5",
 "async.max-pool-size=15"})*/
public class ThreadConfigTest {
    
    @InjectMocks ThreadConfig threadConfig = new ThreadConfig();
    
    @BeforeTest public void setUp() {
    }
    
    @Test public void threadPoolTaskExecutorTest() {
        
        ReflectionTestUtils.setField(threadConfig, "corePoolSize", 5);
        ReflectionTestUtils.setField(threadConfig, "maxPoolSize", 15);
        
        ReflectionTestUtils.setField(threadConfig, "threadNamePrefix", "AsyncCycleTaskExecutor");
        
        threadConfig.threadPoolTaskExecutor();
    }
    
}
