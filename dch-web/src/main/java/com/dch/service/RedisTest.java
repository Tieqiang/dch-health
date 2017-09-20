package com.dch.service;

import com.dch.facade.RedisFacade;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
//import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.OverridesAttribute;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
@Produces("application/json")
@Path("redis")
@Controller
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate ;


    @Autowired
    private RedisFacade redisFacade ;

    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;



    @GET
    @Path("put-value")
    public Response addValueToRedis(@QueryParam("value") String value,@QueryParam("key")String key){
        Long aLong = listOps.leftPush(key, value);
        if(aLong>0){
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("get-value")
    public List<String> getValues(@QueryParam("key") String key){
        List<String> list = listOps.range(key, 0, -1);
        return list;
    }


    @POST
    @Path("save-person")
    public Response savePersion(Person person) throws IOException {
        redisFacade.writeObject(person.getFirstName(),person);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("get-person")
    public Person getPerson(@QueryParam("f") String firstName) throws IOException, ClassNotFoundException {
        return redisFacade.readObject(firstName);
    }

}

class Person implements Serializable{
    private String firstName ;
    private String lastName ;
    private City city ;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}

class City implements Serializable{
    private String city ;
    private String country ;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
