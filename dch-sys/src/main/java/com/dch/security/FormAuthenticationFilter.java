/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dch.security;

import com.dch.util.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2013-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_USERID_PARAM = "userId";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String userIdParam = DEFAULT_USERID_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}
    protected String getUserId(ServletRequest request) {
        return WebUtils.getCleanParam(request, getUserIdParam());
    }

    public String getUserIdParam() {
        return userIdParam;
    }
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
        String userId = getUserId(request);
		if (password==null){
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha,userId);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession httpSession = httpServletRequest.getSession();

		String validateCode = (String)httpSession.getAttribute(httpSession.getId()+"pictureCode");//获取session中的验证码
		//获取参数传递的验证码 暂时注释掉 skq
		String randomCode = httpServletRequest.getParameter("randomCode");
		if(StringUtils.isEmptyParam(validateCode)){
			httpServletRequest.setAttribute("shiroLoginFailure", "randomCodeLoseEffect");//验证码失效
			return true;
		}
		if(StringUtils.isEmptyParam(randomCode)||!randomCode.equals(validateCode)){
			//如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
			httpServletRequest.setAttribute("shiroLoginFailure", "randomCodeError");
			//拒绝访问，不再校验账号和密码
			return true;
		}
		return super.onAccessDenied(request, response);
	}
}