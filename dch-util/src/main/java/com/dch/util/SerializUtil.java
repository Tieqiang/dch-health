package com.dch.util;

import java.io.*;

/**
 * Created by Administrator on 2017/9/20.
 */
public class SerializUtil<T> {

    /**
     * 将一个对象序列成字节码
     * @param t
     * @return
     * @throws IOException
     */
    public byte[] serialize(T t) throws IOException {
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream() ;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(t);
        byte[] bytes = outputStream.toByteArray();
        return bytes ;
    }

    /**
     * 将字节码反序列为一个对象
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T unSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object o = objectInputStream.readObject();
        return (T) o;
    }


}
