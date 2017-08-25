/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dch.util;


import com.dch.vo.Principal;
import com.dch.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Account;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Component
public class UserUtils {
	public static UserUtils userUtils ;


	@PostConstruct
	public void init(){
		userUtils = this ;
	}

	public static UserVo getCurrentUser(){
		UserVo userVo = new UserVo();
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if(principal!=null){
				userVo.setId(principal.getId());
				userVo.setLoginName(principal.getLoginName());
			}
			return  userVo;
		}catch (Exception e){
			e.printStackTrace();
		}
		return userVo;
	}
}
