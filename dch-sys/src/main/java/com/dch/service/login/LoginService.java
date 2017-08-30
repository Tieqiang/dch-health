package com.dch.service.login;

import com.dch.entity.User;
import com.dch.facade.UserFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.security.UsernamePasswordToken;
import com.dch.util.UserUtils;
import com.dch.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/7/19.
 */
@Controller
@Path("sys")
@Produces("application/json")
public class LoginService {

    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserFacade userFacade ;

    /**
     * 获取当前用户
     * @return
     * @throws Exception
     */
    @GET
    @Path("login/success")
    public UserVo getCurrentUser() throws Exception {
        return UserUtils.getCurrentUser();
    }

    @GET
    @Path("login")
    public void login() throws IOException, ServletException {
        try {
            Properties properties = new Properties();
            InputStream inputStream ;
            inputStream=this.getClass().getClassLoader().getResourceAsStream("dchealth.properties");
            properties.load(inputStream);
            String loginPath = properties.getProperty("loginPath") ;
            if(loginPath==null||"".equals(loginPath)){
                loginPath="../index.html";
            }
            httpServletResponse.sendRedirect(loginPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw e ;
        }
    }

    /**
     * 登录成功后返回当前用户
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @POST
    @Path("login")
    public Response loign(@QueryParam("username") String username, @QueryParam("password") String password) throws Exception {
        String exceptionClassName = (String) httpServletRequest.getAttribute("shiroLoginFailure");
        if (exceptionClassName != null) {
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                //最终会抛给异常处理器
                throw new Exception("账号不存在");
            } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
                throw new Exception("用户名/密码错误");
            } else if ("randomCodeError".equals(exceptionClassName)) {
                throw new Exception("验证码错误 ");
            } else if (AuthenticationException.class.getName().equals(exceptionClassName)) {
                throw new Exception("用户授权失败，请等待审核");
            } else {
                //最终在异常处理器生成未知错误
                throw new Exception();
            }
        }
//        UserVo yunUsers = UserUtils.getYunUsers();
        User yunUsers = userFacade.getUserByLoginName(username, "");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
        usernamePasswordToken.setUsername(username);
        usernamePasswordToken.setPassword(password.toCharArray());
        subject.login(usernamePasswordToken);
        UserVo currentUser = UserUtils.getCurrentUser();
        return Response.status(Response.Status.OK).entity(currentUser).build();
    }

    /**
     * 用户退出登录
     * @return
     */
    @Produces("text/plain")
    @POST
    @Path("logout")
    public Response logOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.status(Response.Status.OK).entity("success").build();
    }
}
