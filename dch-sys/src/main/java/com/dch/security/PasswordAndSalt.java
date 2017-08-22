package com.dch.security;

import java.io.Serializable;

/**
 * Created by heren on 2016/10/24.
 */
public class PasswordAndSalt implements Serializable {
    private  String password ;
    private String salt ;


    public PasswordAndSalt(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    public PasswordAndSalt() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PasswordAndSalt{");
        sb.append("password='").append(password).append('\'');
        sb.append(", salt='").append(salt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
