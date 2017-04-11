package org.leon.springboot.demo.controller.rest;

import org.leon.springboot.demo.db.dao.PersonMapper;
import org.leon.springboot.demo.db.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leon on 2017/4/7.
 */
@RestController
@RequestMapping("/api/demo")
public class DemoRestApiController {
    private static final Logger logger = LoggerFactory.getLogger(DemoRestApiController.class);

    @Autowired
    private PersonMapper personMapper;

    @GetMapping("/hello")
    public String hello(){
        Person person = new Person();
        person.setUserid("id");
        person.setUsername("name");
        person.setAge(30);
        personMapper.insert(person);

        return "hello";
    }
}
