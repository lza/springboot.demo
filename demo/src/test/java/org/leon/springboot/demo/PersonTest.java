package org.leon.springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.leon.springboot.demo.db.dao.PersonMapper;
import org.leon.springboot.demo.db.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by leon on 2017/4/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonTest {
    private static final Logger logger = LoggerFactory.getLogger(PersonTest.class);

    @Autowired
    private PersonMapper personMapper;

    @Test
    public void insertTest(){
        Person person = new Person();
        person.setUserid("id");
        person.setUsername("name");
        person.setAge(30);
        personMapper.insert(person);
    }
}
