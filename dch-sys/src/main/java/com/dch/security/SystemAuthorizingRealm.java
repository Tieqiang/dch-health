/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dch.security;
import com.dch.entity.User;
import com.dch.facade.UserFacade;
import com.dch.vo.Principal;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private UserFacade userFacade ;
	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		try {
			User user = userFacade.getUserByLoginName(token.getUsername(),"");
			if(user!=null){
				return  new SimpleAuthenticationInfo(new Principal(user.getId(),user.getLoginName(),user.getLoginName()),token.getCredentials(),getName()) ;
			}else{
				throw new UnknownAccountException("不存在的用户");
			}
		}  catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AuthenticationException){
				throw (AuthenticationException) e;
			}else{
				throw new UnknownAccountException(e.getMessage());
			}
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermission("user:list");
		try {
			User user = userFacade.getUserByLoginName(principal.getLoginName(),"");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		UserVo user = getSystemService().getUserByLoginName(principal.getLoginName());
//		if (user != null) {
//			UserUtils.putCache("user", user);
//
//			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			List<Menu> list = UserUtils.getMenuList();
//			for (Menu menu : list){
//				if (StringUtils.isNotBlank(menu.getPermission())){
//					// 添加基于Permission的权限信息
//					for (String permission : StringUtils.split(menu.getPermission(),",")){
//						info.addStringPermission(permission);
//					}
//				}
//			}
//			// 更新登录IP和时间
//			getSystemService().updateUserLoginInfo(user.getId());
//			return null;
//		} else {
//			return null;
//		}
		return  info ;
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
        HisCredentialsMatcher matcher = new HisCredentialsMatcher() ;
        matcher.setUserFacade(userFacade);

		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}


}
