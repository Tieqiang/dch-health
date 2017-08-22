package com.dch.service.sys;

import com.dch.entity.*;
import com.dch.facade.RoleFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.vo.MenusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */
@Controller
@Produces("application/json")
@Path("sys/role")
public class RoleService {

    @Autowired
    private RoleFacade roleFacade ;


    /**
     * 获取某个角色的所有菜单
     * @param moduleId
     * @param whereHql
     * @return
     */
    @GET
    @Path("get-roles")
    public List<Role> getRoles(@QueryParam("moduleId") String moduleId, @QueryParam("whereHql") String whereHql){
        return roleFacade.getRoles(moduleId,whereHql);
    }

    /**
     * 获取角色的详细信息
     * @param roleId
     * @return
     */
    @GET
    @Path("get-role")
    public Role getRole(@QueryParam("roleId")String roleId) throws Exception {
        Role role = roleFacade.get(Role.class, roleId);
        if("-1".equals(role.getStatus())){
            throw  new Exception("请求对象已被删除");
        }
        return role;
    }


    /**
     * 新增删除、修改角色
     * @param role
     * @return
     */
    @Transactional
    @POST
    @Path("merge")
    public Response mergeRole(Role role){
        Role merge = roleFacade.merge(role);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取角色中的所有用户
     * @param roleId
     * @return
     */
    @GET
    @Path("get-role-users")
    public List<User> getRoleUsers(@QueryParam("roleId") String roleId){
        return roleFacade.getRoleUsers(roleId);
    }


    /**
     * 批量设置角色的用户
     * @param roleId
     * @param users
     * @return
     */
    @POST
    @Path("set-role-users")
    public Response setRoleUsers(@QueryParam("roleId") String roleId,List<User> users) throws Exception {
        roleFacade.setRoleUsers(roleId,users);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","成功")).build();
    }

    /**
     * 根据角色id和用户id删除该角色下的用户
     * @param roleId
     * @param userId
     * @return
     * @throws Exception
     */
    @POST
    @Path("del-role-user")
    public Response delRoleUser(@QueryParam("roleId") String roleId,@QueryParam("userId") String userId) throws Exception {
        roleFacade.delRoleUser(roleId,userId);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","成功")).build();
    }

    /**
     * 获取角色的所有菜单
     * @param roleId
     * @return
     */
    @GET
    @Path("get-role-menus")
    public List<MenusVo> getRoleMenus(@QueryParam("roleId") String roleId){
        return roleFacade.getRoleMenus(roleId);
    }


    /**
     * 设置角色菜单
     * @param roleId
     * @param menusVos
     * @return
     * @throws Exception
     */
    @POST
    @Path("set-role-menus")
    public Response setRoleMenus(@QueryParam("roleId")String roleId,List<MenusVo> menusVos) throws Exception {
        roleFacade.setRoleMenus(roleId,menusVos);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","成功")).build();
    }


    /**
     * 获取角色的操作信息
     * @param roleId
     * @return
     */
    @GET
    @Path("get-role-operations")
    public List<ResourceOperation> getRoleOperations(@QueryParam("roleId") String roleId){
        return roleFacade.getRoleOperations(roleId);
    }

    /**
     * 设置角色的操作
     * @param roleId
     * @param resourceOperations
     * @return
     * @throws Exception
     */
    @POST
    @Path("set-role-operations")
    public Response setRoleOperations(@QueryParam("roleId")String roleId,List<ResourceOperation> resourceOperations) throws Exception {
        roleFacade.setRoleOperations(roleId,resourceOperations);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","成功")).build();
    }

}
