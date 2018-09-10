package com.dch.security;

import com.dch.vo.PlatFormException;
import com.dch.vo.UserVo;
import com.sun.jersey.spi.container.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * Created by sunkqa on 2018/8/1.
 */
public class LoginFilter implements ResourceFilter,ContainerRequestFilter,ContainerResponseFilter{

    @Context
    private HttpServletRequest request;

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        UserVo userVo = (UserVo)request.getSession().getAttribute("user");
        String pathInfo = request.getPathInfo();
        System.out.println("==================userVo"+(userVo==null));
        if(userVo == null && pathInfo.contains("template")){
            throw new PlatFormException("登录时间超时，请重新登录!");
        }
        return containerRequest;
    }

    @Override
    public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {
        return containerResponse;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
    }
}
