package com.dch.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;

/**
 * Created by Administrator on 2017/9/20.
 */
@Component
public class RedisFacade {

    @Autowired
    private RedisTemplate<String,String> redisTemplate ;

    //字符串list
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;

    @Resource(name="redisTemplate")
    private ValueOperations<String,Object> valueOperations ;

    @Resource(name="redisTemplate")
    private ValueOperations<String,byte[]> byteValueOperations ;

    @Resource(name="redisTemplate")
    private ValueOperations<String ,String > stringValueOperations ;


    /**
     * 存储实体类
     * @param key
     * @param t
     * @param <T>
     * @throws IOException
     */
    public <T> void writeObject(String key,T t) throws IOException {
        valueOperations.set(key,t,0);
    }


    /**
     * 读取实体类
     * @param key
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public <T> T readObject(String key) throws IOException, ClassNotFoundException {
        Object o = valueOperations.get(key);

        return (T) o;
    }

    /**
     * 获取字符串内容
     * @param key
     * @param value
     */
    public void writeString(String key,String value){
        stringValueOperations.set(key,value,0);
    }

    /**
     * 读取字符串内容
     * @param key
     * @return
     */
    public String readStringValue(String key){
        return stringValueOperations.get(key);
    }


    /**
     * 写入文件
     * @param key
     * @param file
     * @throws IOException
     */
    public void writeFile(String key,File file) throws IOException {
        InputStream inputStream=new FileInputStream(file);
        byte[] bytes = new byte[]{};
        int read = inputStream.read(bytes);
        byteValueOperations.set(key,bytes,0);
    }

    /**
     * 读取文件
     * @param key
     * @return
     */
    public byte[] readFile(String key){
        byte[] bytes = byteValueOperations.get(key);
        return bytes;
    }

    private static String redisCode = "utf-8";

    /**
     * @param keys
     */
    public Object del(final String... keys) {
        return redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @return
     */
    public Object get(final String key) {
        return redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return new String(connection.get(key.getBytes()), redisCode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

    /**
     * @return
     */
    public Object flushDB() {
        return redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    public RedisTemplate getRedisTemplate(){
        return redisTemplate;
    }
}
