package org.leon.springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by leon on 2017/4/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloTest {
    private static final Logger logger = LoggerFactory.getLogger(HelloTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void helloTest(){
        String url = "http://localhost:8080/demo/api/demo/hello";
        String response = restTemplate.getForObject(url, String.class);
        logger.debug("response:" + response);
    }
}
