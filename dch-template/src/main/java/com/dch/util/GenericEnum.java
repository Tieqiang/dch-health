package com.dch.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sunkqa on 2018/3/9.
 */
public class GenericEnum<E> implements Serializable {

    private final TreeMap<E,String> enumMap = new TreeMap();

    public String getName(E value){
        return this.enumMap.get(value);
    }

    public E getValue(String name) throws Exception{
        Object value = null;
        if(enumMap.containsValue(name)){
            Iterator iterator = enumMap.keySet().iterator();
            while (iterator.hasNext()){
                Object key = iterator.next();
                String ename = enumMap.get(key);
                if(ename.equals(name)){
                    value = key;
                    break;
                }
            }
        }else{
            throw new Exception("未知枚举类型");
        }
        return (E)value;
    }

    public String[] getAllNames(){
        String[] names = new String[enumMap.size()];
        enumMap.values().toArray(names);
        return names;
    }

    public void putEnum(E value, String name) {
        this.enumMap.put(value, name);
    }

    public Map<E, String> getAllEnum() {
        return this.enumMap;
    }
}
