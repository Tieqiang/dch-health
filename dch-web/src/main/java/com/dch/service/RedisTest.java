package com.dch.service;

import com.dch.entity.MrFile;
//import com.dch.facade.RedisFacade;
import com.dch.facade.common.BaseFacade;
import org.jboss.logging.annotations.Pos;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
//import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.OverridesAttribute;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
//@Produces("application/json")
//@Path("redis")
//@Controller
public class RedisTest {
//
//    @Autowired
//    private RedisTemplate<String,String> redisTemplate ;
//
//
//    @Autowired
//    private RedisFacade redisFacade ;
//
//    @Autowired
//    private BaseFacade baseFacade ;
//
//    @Resource(name="redisTemplate")
//    private ListOperations<String, String> listOps;
//
//
//
//    @GET
//    @Path("put-value")
//    public Response addValueToRedis(@QueryParam("value") String value,@QueryParam("key")String key){
//        Long aLong = listOps.leftPush(key, value);
//        if(aLong>0){
//            return Response.status(Response.Status.OK).build();
//        }
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//    }
//
//    @GET
//    @Path("get-value")
//    public List<String> getValues(@QueryParam("key") String key){
//        List<String> list = listOps.range(key, 0, -1);
//        return list;
//    }
//
//
//    @POST
//    @Path("save-person")
//    public Response savePersion(Person person) throws IOException {
//        redisFacade.writeObject(person.getFirstName(),person);
//        return Response.status(Response.Status.OK).build();
//    }
//
//    @GET
//    @Path("get-person")
//    public Person getPerson(@QueryParam("f") String firstName) throws IOException, ClassNotFoundException {
//        return redisFacade.readObject(firstName);
//    }
//
//    @Transactional
//    @Path("merge")
//    @GET
//    public Response clearData(){
//        List<MrFile> all = baseFacade.findAll(MrFile.class);
//        int i = 0 ;
//        for(MrFile file:all){
//            clearFile(file);
//            System.out.println("处理数据第"+i+"条");
//            baseFacade.merge(file);
//            i++;
//        }
//        return Response.status(Response.Status.OK).build();
//    }
//
//    private void clearFile(MrFile file) {
//        String fileContent = file.getFileContent();
//        if(fileContent==null||"".equals(fileContent)){
//            return ;
//        }
//
//
//        Document document = Jsoup.parse(fileContent);
//        Iterator<Element> img = document.getElementsByTag("img").iterator();
//        while(img.hasNext()){
//            Element next = img.next();
//            String src = next.attr("src");
//            if(!src.startsWith("/")){
//                src="/"+src;
//            }
//            next.attr("src",src);
//        }
//
//
//        Elements content_share = document.getElementsByClass("content_share");
//        Elements readCT = document.getElementsByClass("readCT");
//        content_share.remove();
//        readCT.remove();
//
//        String html = document.html();
//        file.setFileContent(html);
//    }

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
