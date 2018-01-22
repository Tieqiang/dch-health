package com.dch.service;

import com.dch.vo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Controller
@Produces("application/json")
@Path("mongo")
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate ;

    @GET
    public void mongo(){
        Person person = new Person("zhangsan",28);
        mongoTemplate.insert(person);
    }


}
