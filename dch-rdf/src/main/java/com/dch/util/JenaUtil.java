package com.dch.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by sunkqa on 2018/5/9.
 */
public class JenaUtil {
    private static String rdfdb = "";
    private static final String DEFAULT_CONFIG_FILE = "rdfdb.properties";
    private static Properties prop = null;
    private static ResourceLoader loader = ResourceLoader.getInstance();
    private static ConcurrentMap<String, String> paramMap = new ConcurrentHashMap<String, String>();

    public static String getNameByRdfId(String rdfId){
        try{
            if("".equals(rdfId)||rdfId==null){
                return rdfId;
            }
            if(!rdfId.contains("#")){
                return rdfId;
            }
            int index = rdfId.lastIndexOf("#");
            return rdfId.substring(index+1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rdfId;
    }

    public static String getMiddleKey(Map<String,Map> resultMap){
        String middleKey = "";
        Map<String,Integer> map = new HashMap<>();
        for(String key:resultMap.keySet()){
            if(map.containsKey(key)){
                map.put(key,map.get(key)+1);
            }else{
                map.put(key,1);
            }
            Map innerMap = resultMap.get(key);
            for(Object inkey:innerMap.keySet()){
                String innerKey = inkey.toString();
                if(map.containsKey(innerKey)){
                    map.put(innerKey,map.get(innerKey)+1);
                }else{
                    map.put(innerKey,1);
                }
            }
        }
        for(String fkey:map.keySet()){
            Integer value = map.get(fkey);
            if(value>1){
                String firstKey = resultMap.get(fkey).keySet().isEmpty()?"":resultMap.get(fkey).keySet().iterator().next().toString();
                if(map.get(firstKey)==1){
                    middleKey = fkey;
                }
            }
        }
        return middleKey;
    }

    public static String getRdfdb(){
        try {
            String os = System.getProperty("os.name");
            if(StringUtils.isEmptyParam(rdfdb)){
                if(os.toLowerCase().startsWith("win")){
                    rdfdb = getStringByKey("windowdb.loc");
                }else{
                    rdfdb = getStringByKey("linuxdb.loc");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return rdfdb;
    }

    public static String getStringByKey(String key) {
        return getStringByKeyAndPropName(key, DEFAULT_CONFIG_FILE);
    }

    public static String getStringByKeyAndPropName(String key, String propName) {
        try {
            prop = loader.getPropFromProperties(propName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        key = key.trim();
        if (!paramMap.containsKey(key)) {
            if (prop.getProperty(key) != null) {
                paramMap.put(key, prop.getProperty(key));
            }
        }
        return paramMap.get(key);
    }

    public static String delHtmlTag(String str){
        String newstr = "";
        newstr = str.replaceAll("<[.[^>]]*>","");
        newstr = newstr.replaceAll(" ", "");
        return newstr;
    }

    public static String getUID(){
        String uid = UUID.randomUUID().toString();
        uid = uid.replace("-","");
        return uid;
    }
}
