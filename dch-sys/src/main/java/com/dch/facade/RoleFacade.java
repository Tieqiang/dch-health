package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.vo.MenusVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
@Component
public class RoleFacade extends BaseFacade {
    /**
     * 获取某个角色的所有菜单
     * @param moduleId
     * @param whereHql
     * @return
     */
    public List<Role> getRoles(String moduleId,String whereHql){
        String hql = "from Role as r where r.moduleId='"+moduleId+"' and r.status<>'-1'" ;
        if (whereHql!=null&&!"".equals(whereHql)){
            hql += " and "+whereHql;
        }
        return createQuery(Role.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 获取角色的详细信息
     * @param roleId
     * @return
     */
    public Role getRole(String roleId) throws Exception {
        Role role = get(Role.class, roleId);
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
    public Response mergeRole(Role role){
        Role merge = merge(role);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取角色中的所有用户
     * @param roleId
     * @return
     */
    public List<User> getRoleUsers(String roleId){
        String hql = "select u from User as u ,UserVsRole as rvu where rvu.userId=u.id and rvu.roleId='"+roleId+"'" +
                " and u.status<>'-1'" ;
        return createQuery(User.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 批量设置角色的用户
     * @param roleId
     * @param users
     * @return
     */
    @Transactional
    public void setRoleUsers(String roleId,List<User> users) throws Exception {
        //String delHql = "delete from UserVsRole where roleId='"+roleId+"'";
        //baseFacade.getEntityManager().createQuery(delHql).executeUpdate();
        //skq begin
        List<String> userIds = new ArrayList<String>();
        for(User user:users){
            userIds.add(user.getId());
        }
        String queryHql = "select uv.userId from UserVsRole uv  where  uv.roleId='"+roleId+"'";
        List<String> dbUserIds  = createQuery(String.class,queryHql,new ArrayList<Object>()).getResultList();
        for(String userId:dbUserIds){
            userIds.remove(userId);
        }
        //skq end
        if(roleId==null||"".equals(roleId)){
            throw new Exception("传递的角色信息不能为空");
        }
        for(String userId:userIds){
            UserVsRole userVsRole = new UserVsRole();
            userVsRole.setRoleId(roleId);
            userVsRole.setUserId(userId);
            merge(userVsRole);
        }
    }

    /**
     * 根据角色id和用户id删除该角色下的用户
     * @param roleId
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional
    public void delRoleUser(String roleId,String userId) throws Exception {
        if(roleId==null||"".equals(roleId)){
            throw new Exception("传递的角色信息不能为空");
        }
        if(userId==null||"".equals(userId)){
            throw new Exception("传递的用户信息不能为空");
        }
        String delHql = "delete from UserVsRole where roleId = '"+roleId+"' and userId ='"+userId+"'";
        excHql(delHql);
    }

    /**
     * 获取角色的所有菜单
     * @param roleId
     * @return
     */
    public List<MenusVo> getRoleMenus(String roleId){
        String hql = "select m from MenuVsRole as mvr,Menu as m  where mvr.roleId='"+roleId+"' and m.id=mvr.menuId and m.status<>'-1'" ;
        List<Menu> menus = createQuery(Menu.class, hql, new ArrayList<Object>()).getResultList();
        List<MenusVo> menusVos = new ArrayList<MenusVo>();
        for(Menu menu:menus){
            MenusVo menusVo = new MenusVo();
            menusVo.setMenu(menu);
            String hql1 = "from FunctionMenu where menuId='"+menu.getId()+"' and roleId='"+roleId+"'";
            List<FunctionMenu> functionMenus = createQuery(FunctionMenu.class,hql1,new ArrayList<Object>()).getResultList();
            menusVo.setFunctionMenus(functionMenus);
            menusVos.add(menusVo);
        }
        return menusVos;
    }


    /**
     * 设置角色菜单
     * @param roleId
     * @param menusVos
     * @return
     * @throws Exception
     */
    @Transactional
    public void setRoleMenus(String roleId,List<MenusVo> menusVos) throws Exception {

        String delHql = "delete MenuVsRole where roleId='"+roleId+"'" ;
        String delMenuFunctions = "delete FunctionMenu where roleId='"+roleId+"'";

        getEntityManager().createQuery(delHql).executeUpdate();
        getEntityManager().createQuery(delMenuFunctions).executeUpdate();

        if(roleId==null||"".equals(roleId)){
            throw new Exception("传递的角色信息不能为空");
        }

        for (MenusVo menusVo:menusVos){
            Menu menu = menusVo.getMenu();
            List<FunctionMenu> functionMenus = menusVo.getFunctionMenus();
            for(FunctionMenu functionMenu:functionMenus){
                FunctionMenu newFunctionMenu = new FunctionMenu();
                newFunctionMenu.setRoleId(roleId);
                newFunctionMenu.setMenuId(menu.getId());
                newFunctionMenu.setOperation(functionMenu.getOperation());
                newFunctionMenu.setOperationCode(menu.getMenuCode()+":"+functionMenu.getOperation());
                merge(newFunctionMenu);
            }
            MenuVsRole menuVsRole = new MenuVsRole();
            menuVsRole.setRoleId(roleId);
            menuVsRole.setMenuId(menu.getId());
            merge(menuVsRole);
        }
    }


    /**
     * 获取角色的操作信息
     * @param roleId
     * @return
     */
    public List<ResourceOperation> getRoleOperations(String roleId){
        String hql = "select fo from RoleVsResourceOperation as rvo ,ResourceOperation as fo " +
                " where rvo.resourceOperationId = fo.id" +
                " and rvo.roleId='"+roleId+"'" +
                " and fo.status<>'-1'" ;
        return createQuery(ResourceOperation.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 设置角色的操作
     * @param roleId
     * @param resourceOperations
     * @return
     * @throws Exception
     */
    @Transactional
    public void setRoleOperations(String roleId, List<ResourceOperation> resourceOperations) throws Exception {
        String delHql = "delete RoleVsResourceOperation where roleId='"+roleId+"'" ;
        excHql(delHql);
        if(roleId==null||"".equals(roleId)){
            throw new Exception("传递的角色信息不能为空");
        }
        for(ResourceOperation resourceOperation : resourceOperations){
            RoleVsResourceOperation roleVsResourceOperation = new RoleVsResourceOperation();
            roleVsResourceOperation.setRoleId(roleId);
            roleVsResourceOperation.setResourceOperationId(resourceOperation.getId());
            merge(roleVsResourceOperation);
        }
    }

}
