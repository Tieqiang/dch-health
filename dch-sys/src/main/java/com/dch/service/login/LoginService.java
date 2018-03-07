package com.dch.service.login;

import com.dch.entity.User;
import com.dch.facade.UserFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Controller
@Path("sys")
@Produces("application/json")
public class LoginService {
    public static final String pictureCodeToLogin = "pictureCodeToLogin";
    public static final String picture_Code = "pictureCode";
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
            } else if ("randomCodeLoseEffect".equals(exceptionClassName)) {
                throw new Exception("验证码失效，请重新填写 ");
            }else if (AuthenticationException.class.getName().equals(exceptionClassName)) {
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
        try{
            subject.login(usernamePasswordToken);
        }catch (Exception e){
            throw new Exception("输入密码错误！");
        }
        httpServletRequest.getSession().removeAttribute(httpServletRequest.getSession().getId()+"pictureCode");
        UserVo currentUser = UserUtils.getCurrentUser();
        return Response.status(Response.Status.OK).entity(currentUser).build();
    }

    /**
     * 用户退出登录
     * @return
     */
//    @Produces("text/plain")
    @POST
    @Path("logout")
    public Response logOut(){
        System.out.println("我的请求");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","退出成功")).build();
    }

    /**
     * 获取注册图形验证码
     * @param request
     * @return
     */
    @GET
    @Path("get-picture-code")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPictureCode(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception{
        StringBuffer pictureCode = new StringBuffer("");
        int width = 120;
        int height = 41;
        int lines = 10;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 设置字体
        g.setFont(new Font("宋体", Font.BOLD, 20));
        // 随机数字
        Random r = new Random(new Date().getTime());
        for (int i = 0; i < 4; i++) {
            int a = r.nextInt(10);
            int y = 10 + r.nextInt(20);// 10~30范围内的一个整数，作为y坐标

            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);

            g.drawString("" + a, 5 + i * width / 4, y);
            pictureCode.append(a);
        }
        System.out.println("==="+pictureCode.toString());
        // 干扰线
        for (int i = 0; i < lines; i++) {
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
        }
        g.dispose();// 类似于流中的close()带动flush()---把数据刷到img对象当中
        try{
            HttpSession httpSession = request.getSession();
            request.getSession().setAttribute(httpSession.getId()+ picture_Code,pictureCode.toString());
            request.getSession().setAttribute(httpSession.getId()+pictureCodeToLogin,pictureCode.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        ImageIO.write(img, "JPG", response.getOutputStream());
        return Response.status(Response.Status.OK).entity(response.getOutputStream()).header("Content-disposition","attachment;filename="+"图形码")
                .header("Cache-Control","no-cache").build();
    }

//    @GET
//    @Path("get-new-code")
//    public void startValidate(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception{
//        List<String> list = new ArrayList<>();
//        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(), GeetestConfig.isnewfailback());
//        String resStr = "{}";
//        // 自定义userid
//        String userid = request.getSession()+"CaptchaCode";
//        //进行验证预处理
//        HashMap<String,String> map = new HashMap<>();
//        map.put("user_id",userid);
//        int gtServerStatus = gtSdk.preProcess(map);
//
//        //将服务器状态设置到session中
//        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
//        //将userid设置到session中
//        request.getSession().setAttribute("userid", userid);
//
//        resStr = gtSdk.getResponseStr();
//        PrintWriter out = response.getWriter();
//        out.println(resStr);
////        list.add(resStr);
////        return list;
//    }
}
