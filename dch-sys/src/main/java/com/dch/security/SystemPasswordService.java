package com.dch.security;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Created by heren on 2016/10/24.
 */
public class SystemPasswordService {

    private static String ALGORITHMNAME= Md5Hash.ALGORITHM_NAME;
    private static int HASHITERATIONS=1024 ;

    public static PasswordAndSalt enscriptPassword(String userName,String password ){
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        SimpleHash simpleHash = new SimpleHash(ALGORITHMNAME,password,userName+salt,HASHITERATIONS) ;
        PasswordAndSalt passwordAndSalt = new PasswordAndSalt(simpleHash.toHex(),salt) ;
        return passwordAndSalt ;
    }

    public static String enscriptPasswordWithSalt(String salt,String username,String password){
        SimpleHash simpleHash = new SimpleHash(ALGORITHMNAME,password,username+salt,HASHITERATIONS) ;
        return simpleHash.toHex();
    }


    public static void main(String[] args) {
        //1f400473bd7f5df0226132fb771fef8c
        String passwordAndSalt = enscriptPasswordWithSalt("c892be33d7be4ba1b9d2f7032a5dd153","aaa123","aaa123aaa123");
        System.out.println(passwordAndSalt);
    }
}
