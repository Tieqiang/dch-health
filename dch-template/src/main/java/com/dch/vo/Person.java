package com.dch.vo;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

public class Person {

    private String id;
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
    }

    public static void main(String[] args) throws Exception {

//        MongoOperations mongoOps = new MongoTemplate(new MongoClient("10.1.85.21"), "database");
//        mongoOps.insert(new Person("Joe", 34));

//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append("nihao,shijie");
//        stringBuffer.append(",");
//        System.out.println(stringBuffer.toString());
//        stringBuffer.delete(stringBuffer.lastIndexOf(","),stringBuffer.lastIndexOf(",")+1);
//        System.out.println(stringBuffer.toString());
//        mongoOps.dropCollection("person");
    }
}