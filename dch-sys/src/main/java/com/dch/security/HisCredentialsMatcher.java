package com.dch.security;

import com.dch.entity.User;
import com.dch.facade.UserFacade;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * shiro密码验证类
 * 重写doCredentialsMatch方法，返回true验证通过
 * 返回false 验证不通过
 * Created by heren on 2015/10/22.
 */
@Service
public class HisCredentialsMatcher extends SimpleCredentialsMatcher {


    @Autowired
    private UserFacade userFacade ;

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token ;
        String username = usernamePasswordToken.getUsername() ;
        char[] password1 = usernamePasswordToken.getPassword();
        String password = new String(password1);
        try {
            User yunUsers = userFacade.getUserByLoginName(username,"");
            String dbPass = yunUsers.getPassword();
            String newPass = SystemPasswordService.enscriptPasswordWithSalt(yunUsers.getSalt(),username,password);
            return dbPass.equals(newPass);
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

}
