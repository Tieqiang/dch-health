package com.dch.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;

/**
* 实体类和JSON对象之间相互转化（依赖包jackson-all-1.7.6.jar、jsoup-1.5.2.jar）
* @author wck
*
*/
public class JSONUtil {
   /**
    * 将json转化为实体POJO
    * @param jsonStr
    * @param obj
    * @return
    */
   public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
       T t = null;
       try {
           ObjectMapper objectMapper = new ObjectMapper();
           t = objectMapper.readValue(jsonStr,
                   obj);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return t;
   }

   /**
    * 将实体POJO转化为JSON
    * @param obj
    * @return
    * @throws JSONException
    * @throws IOException
    */
   public static<T> JSONObject objectToJson(T obj) throws JSONException, IOException {
       ObjectMapper mapper = new ObjectMapper();
       // Convert object to JSON string
       String jsonStr = "";
       try {
           jsonStr =  mapper.writeValueAsString(obj);
       } catch (IOException e) {
           throw e;
       }
       return new JSONObject(jsonStr);
   }
  /**
    * 将实体POJO转化为JSON
    * @param obj
    * @return
    * @throws JSONException
    * @throws IOException
    */
   public static String objectToJsonString(Object obj) throws JSONException, IOException {
       ObjectMapper mapper = new ObjectMapper();
       // Convert object to JSON string
       String jsonStr = "";
       try {
           jsonStr =  mapper.writeValueAsString(obj);
       } catch (IOException e) {
           throw e;
       }
       return jsonStr;
   }

}