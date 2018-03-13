package com.dch.service.sys;

import com.dch.entity.*;
import com.dch.facade.UserFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.security.PasswordAndSalt;
import com.dch.security.SystemPasswordService;
import com.dch.util.UserUtils;
import com.dch.vo.MenusVo;
import com.dch.vo.PriVo;
import com.dch.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/7/19.
 */
@Path("sys/user")
@Controller
@Produces("application/json")
public class UserService {

    @Autowired
    private BaseFacade baseFacade ;

    @Autowired
    private UserFacade userFacade;
    /**
     * 获取当前登录用户
     * @return
     * @throws Exception
     */
    public UserVo getCurrentUser() throws Exception {
        UserVo yunUsers = UserUtils.getCurrentUser();
        return yunUsers;
    }

    @GET
    @Path("get-current-user")
    public User getCurrentUserInfo() throws Exception{
        UserVo userVo = getCurrentUser();
        String userId = userVo.getId();
        if(userId==null||"".equals(userId)){
            return new User();
        }
        User user = getUserById(userId, "");
        return user;
    }

    /**
     * 用户信息注册
     * @param user
     * @return
     */
    @POST
    @Path("regist")
    @Transactional
    public Response regist(User user) throws Exception {
        PasswordAndSalt passwordAndSalt = SystemPasswordService.enscriptPassword(user.getLoginName(), user.getPassword());
        user.setPassword(passwordAndSalt.getPassword());
        user.setSalt(passwordAndSalt.getSalt());
        user.setStatus("1");
        /*
        To do
        配置用户的角色 和 模块 从配置文件中获取
        */
        User merge = userFacade.merge(user);
        Properties properties = new Properties() ;
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("dchealth.properties");
        properties.load(resourceAsStream);
        String defaultRoleId =properties.getProperty("defaultRoleId");
        String defaultModuleId =properties.getProperty("defaultModuleId");
        if(defaultRoleId!=null && !"".equals(defaultRoleId)){
            UserVsRole userVsRole = new UserVsRole();
            userVsRole.setRoleId(defaultRoleId);
            userVsRole.setUserId(merge.getId());
            userFacade.merge(userVsRole);
        }
        if(defaultModuleId!=null && !"".equals(defaultModuleId)){
            UserVsModule userVsModule = new UserVsModule();
            userVsModule.setModuleId(defaultModuleId);
            userVsModule.setUserId(merge.getId());
            userFacade.merge(userVsModule);
        }
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 根据登录名获取用户信息
     * @param loginName
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-user-by-login-name")
    public User getUserByLoginName(@QueryParam("loginName") String loginName,@QueryParam("whereHql") String whereHql) throws Exception {
        return userFacade.getUserByLoginName(loginName,whereHql);
    }

    /**
     * 根据ID获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-user-by-id")
    public User getUserById(@QueryParam("userId") String userId,@QueryParam("whereHql") String whereHql) throws Exception {
        return userFacade.getYunUserById(userId,whereHql);
    }

    /**
     * 修改密码
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @POST
    @Transactional
    @Path("merge-pwd")
    public Response mergePwd(@QueryParam("oldPwd") String oldPwd, @QueryParam("newPwd") String newPwd,
                              @QueryParam("userId") String userId) throws Exception{
        User yunUsers = userFacade.getYunUserById(userId,"");
        String loginName = yunUsers.getLoginName();
        String passwordWithSalt = SystemPasswordService.enscriptPasswordWithSalt(yunUsers.getSalt(), loginName, oldPwd);
        String oldDbPassword = yunUsers.getPassword() ;
        if(passwordWithSalt.equals(oldDbPassword)){
            PasswordAndSalt passwordAndSalt = SystemPasswordService.enscriptPassword(loginName, newPwd);
            yunUsers.setPassword(passwordAndSalt.getPassword());
            yunUsers.setSalt(passwordAndSalt.getSalt());
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return Response.status(Response.Status.OK).entity(userFacade.merge(yunUsers)).build();
        }else{
            throw new Exception("原密码错误！");
        }
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @POST
    @Transactional
    @Path("reset-pwd")
    public Response resetPwd(@QueryParam("userId") String userId) throws Exception {
        User yunUsers = userFacade.getYunUserById(userId,"") ;
        Properties properties = new Properties() ;
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("dchealth.properties");
        properties.load(resourceAsStream);
        String newPassword =properties.getProperty("newPassword");
        if("".equals(newPassword)||null==newPassword){
            newPassword = "123456" ;
        }
        if (yunUsers!=null){
            PasswordAndSalt passwordAndSalt = SystemPasswordService.enscriptPassword(yunUsers.getLoginName(), newPassword);
            yunUsers.setPassword(passwordAndSalt.getPassword());
            yunUsers.setSalt(passwordAndSalt.getSalt());
            userFacade.merge(yunUsers);
        }
        return Response.status(Response.Status.OK).entity(yunUsers).build();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response merge(User user) throws Exception {
        String id = user.getId();
        if(id==null||"".equals(id)){
            throw new Exception("获取不到原信息的ID");
        }
        User users = userFacade.merge(user);
        return Response.status(Response.Status.OK).entity(users).build();
    }

    /**
     * 获取用户权限信息
     * @param moduleId
     */
    @GET
    @Path("get-user-pri")
    public PriVo getUserPriviliges(@QueryParam("moduleId") String moduleId) throws Exception{
        PriVo priVo = new PriVo();
        UserVo user = getCurrentUser();
        Module module = userFacade.get(Module.class,moduleId);
        User u = userFacade.get(User.class,user.getId());
        List<MenusVo> menuList = getMenuList(user.getId(),moduleId);
        List<ResourceOperation> resourceOperationList = getFunctionOperationList(user.getId(),moduleId);
        priVo.setUser(u);
        priVo.setModule(module);
        priVo.setMenus(menuList);
        priVo.setResourceOperations(resourceOperationList);
        return priVo;
    }

    /**
     * 分配用户模块
     * @param userId
     * @param moduleList
     * @return
     */
    @POST
    @Path("set-user-modules")
    public Response setUserModules(@QueryParam("userId") String userId,List<Module> moduleList) throws Exception{
        try {
            userFacade.setUserModules(userId,moduleList);
        }catch (Exception e){
            throw e;
        }
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","分配用户模块成功")).build();
    }

    /**
     * 获取用户某个模块角色
     * @param userId
     * @param moduleId
     * @return
     */
    @GET
    @Path("get-user-roles-by-module")
    public List<Role> getUserRolesByModule(@QueryParam("userId") String userId, @QueryParam("moduleId") String moduleId){
        return userFacade.getUserRolesByModule(userId,moduleId);
    }

    /**
     *用户角色分配
     * @param userId 用户ID
     * @param moduleId 模块ID
     * @param roleList 角色对象数组
     * @return
     */
    @POST
    @Path("set-user-role")
    public Response setUserRoles(@QueryParam("userId") String userId,@QueryParam("moduleId") String moduleId,List<Role> roleList) throws Exception{
        try {
            userFacade.setUserRoles(userId,moduleId,roleList);
        }catch (Exception e){
            throw e;
        }
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","用户角色分配")).build();
    }

    /**
     * 获取用户模块信息
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-user-modules")
    public List<Module> getUserModules() throws Exception{
        UserVo user = getCurrentUser();
        return userFacade.getUserModules(user.getId());
    }
    /**
     * 根据模块id获取用户权限信息
     * @param userId
     * @return
     */
    private List<MenusVo> getMenuList(String userId,String moduleId){
        List<Menu> menus = userFacade.getMenusByUserIdAndModuleId(userId,moduleId);
        List<MenusVo> menusVos = new ArrayList<MenusVo>();
        for(Menu menu:menus){
            MenusVo menusVo = new MenusVo();
            menusVo.setMenu(menu);
            List<FunctionMenu> functionMenus = userFacade.getFunctionMenusByUserIdAndModuleId(userId,moduleId,menu.getId());
            menusVo.setFunctionMenus(functionMenus);
            menusVos.add(menusVo);
        }
        return menusVos;

    }
    /**
     * 根据模块id获取用户操作权限信息
     * @param moduleId
     * @param userId
     * @return
     */
    public List<ResourceOperation> getFunctionOperationList(String userId,String moduleId){
        return userFacade.getFunctionOperationList(userId,moduleId);
    }

    /**
     *获取用户列表
     * @param userName 真实姓名
     * @param loginName 登录名
     * @return
     */
    @GET
    @Path("list")
    public Page<User> getUsers(@QueryParam("userName")String userName, @QueryParam("loginName")String loginName,
                                @QueryParam("perPage")int perPage, @QueryParam("currentPage") int currentPage){
        return userFacade.getUsers(userName,loginName,perPage,currentPage);
    }

    /**
     *根据用户ID获取该用户模块信息
     * @param userId 用户ID
     * @return
     */
    @GET
    @Path("get-user-modules-by-id")
    public List<Module> getUserModulesById(@QueryParam("userId") String userId){
        return userFacade.getUserModulesById(userId);
    }

    /**
     *根据登录名 检查用户是否存在
     * @param loginName 登录名
     * @return
     */
    @GET
    @Path("checkUserExist")
    public Response checkUserExist(@QueryParam("loginName") String loginName){
        return Response.status(Response.Status.OK).entity(userFacade.checkUserExist(loginName)).build();
    }
}
