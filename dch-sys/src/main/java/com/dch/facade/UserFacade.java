package com.dch.facade;

import com.dch.entity.*;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@Component
public class UserFacade extends BaseFacade {
    /**
     * 根据登录名获取用户信息
     * @param loginName
     * @return
     * @throws Exception
     */
    public User getUserByLoginName(String loginName, String whereHql) throws Exception {
        String hql = "from User where status<> '-1' and loginName='"+loginName+"'";
        if(whereHql!=null && !"".equals(whereHql)){
            hql +=" "+whereHql;
        }
        List<User> resultList = null;
        try {
            resultList = createQuery(User.class, hql, new ArrayList<>()).getResultList();
        }catch (Exception e){
            throw new Exception("查询拼接where语句错误");
        }
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("不存在的用户名");
        }
    }

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public User getYunUserById(String id,String whereHql) throws Exception {
        String hql = "from User where status<> '-1' and id='"+id+"'";
        if(whereHql!=null && !"".equals(whereHql)){
            hql +=" "+whereHql;
        }
        List<User> resultList = null;
        try {
            resultList = createQuery(User.class, hql, new ArrayList<>()).getResultList();
        }catch (Exception e){
            throw new Exception("查询拼接where语句错误");
        }
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }else{
            throw new Exception("不存在的用户名");
        }
    }

    /**
     *根据用户ID获取该用户模块信息
     * @param userId 用户ID
     * @return
     */
    public List<Module> getUserModulesById(String userId) {
        String hql = "select md from Module as md,UserVsModule as uvm where md.id = uvm.moduleId and md.status<> '-1' " +
                " and uvm.userId = '"+userId+"'";
        return createQuery(Module.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     *获取用户列表
     * @param userName 真实姓名
     * @param loginName 登录名
     * @return
     */
    public List<User> getUsers(String userName, String loginName) {
        String hql = " from User where status<> '-1' ";
        if(!StringUtils.isEmptyParam(userName)){
            hql += " and userName like '%"+userName+"%'";
        }
        if(!StringUtils.isEmptyParam(loginName)){
            hql += " and loginName = '"+loginName+"' or userName like '%"+loginName+"%'";
        }
        return createQuery(User.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据模块id获取用户操作权限信息
     * @param moduleId
     * @param userId
     * @return
     */
    public List<ResourceOperation> getFunctionOperationList(String userId, String moduleId) {
        String hql = "select distinct fo from Role as role,RoleVsResourceOperation as rvr,ResourceOperation as fo,UserVsRole as uvr " +
                " where role.id = rvr.roleId and rvr.resourceOperationId =fo.id  " +
                " and role.status<> '-1' and fo.status<> '-1'" +
                " and role.moduleId = '"+moduleId+"'" +
                " and role.id=uvr.roleId and uvr.userId='"+userId+"'";
        return createQuery(ResourceOperation.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据模块id获取用户菜单信息
     * @param userId 用户id
     * @param moduleId 模块id
     * @return
     */
    public List<Menu> getMenusByUserIdAndModuleId(String userId, String moduleId) {
        String hql = "select distinct menu from Menu as menu,MenuVsRole as mvr,UserVsRole as uvr where menu.status<> '-1' " +
                " and  menu.id = mvr.menuId " +
                " and mvr.roleId=uvr.roleId " +
                " and uvr.userId='"+userId+"' " +
                " and menu.moduleId='"+moduleId+"'" ;
        return createQuery(Menu.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据模块id,用户id和菜单id获取用户操作权限信息
     * @param userId 用户id
     * @param moduleId 模块id
     * @param menuId 菜单id
     * @return
     */
    public List<FunctionMenu> getFunctionMenusByUserIdAndModuleId(String userId, String moduleId,String menuId) {
        String hql1 = "select distinct fm from FunctionMenu as fm ,UserVsRole as uvr,Role as r  where fm.menuId='"+menuId+"'" +
                " and uvr.roleId=r.id and uvr.userId='"+userId+"' and r.moduleId='"+moduleId+"'";
        return createQuery(FunctionMenu.class,hql1,new ArrayList<Object>()).getResultList();
    }

    /**
     *根据用户id获取用户模块信息
     * @param userId 用户id
     * @return
     */
    public List<Module> getUserModules(String userId) {
        String hql = "select md from Module as md,UserVsModule as uvm where md.id = uvm.moduleId and md.status<> '-1' " +
                " and uvm.userId = '"+userId+"'";
        return createQuery(Module.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     *根据用户id和模块id删除其对应的角色
     * @param userId 用户id
     * @param moduleId 模块id
     */
    @Transactional
    public void deleteUserVsRole(String userId, String moduleId) {
        String delHql = "delete from UserVsRole as uvr where uvr.userId = '"+userId+"' and exists (select id from Role " +
                " where id = uvr.roleId and moduleId = '"+moduleId+"') ";
        excHql(delHql);
    }

    @Transactional
    public void deleteUserVsModule(String userId) {
        String delHql = "delete from UserVsModule  where userId = '"+userId+"'";
        excHql(delHql);
    }

    /**
     * 分配用户模块
     * @param userId 用户id
     * @param moduleList 模块集合
     */
    @Transactional
    public void setUserModules(String userId, List<Module> moduleList) {
        deleteUserVsModule(userId);
        for(Module module:moduleList){
            UserVsModule userVsModule = new UserVsModule();
            userVsModule.setModuleId(module.getId());
            userVsModule.setUserId(userId);
            merge(userVsModule);
        }
    }

    /**
     * 获取用户某个模块角色
     * @param userId 用户id
     * @param moduleId 模块id
     * @return
     */
    public List<Role> getUserRolesByModule(String userId, String moduleId) {
        String hql = "select role from UserVsRole as uvr,Role as role where uvr.roleId = role.id " +
                " and uvr.userId = '"+userId+"' and role.moduleId = '"+moduleId+"' and role.status<> '-1'";
        return createQuery(Role.class, hql, new ArrayList<Object>()).getResultList();
    }

    /**
     *用户角色分配
     * @param userId 用户id
     * @param moduleId 模块id
     * @param roleList 角色集合
     */
    @Transactional
    public void setUserRoles(String userId, String moduleId, List<Role> roleList) {
        deleteUserVsRole(userId,moduleId);
        for(Role role:roleList){
            UserVsRole userVsRole = new UserVsRole();
            userVsRole.setUserId(userId);
            userVsRole.setRoleId(role.getId());
            merge(userVsRole);
        }
    }

    /**
     *根据登录名 检查用户是否存在
     * @param loginName
     * @return
     */
    public ReturnInfo checkUserExist(String loginName) {
        ReturnInfo returnInfo = null;
        try {
            String hql = "from User where status<> '-1' and loginName='"+loginName+"'";
            List<User> userList = createQuery(User.class,hql,new ArrayList<Object>()).getResultList();
            if(userList!=null && !userList.isEmpty()){
                returnInfo = new ReturnInfo("true","用户名已存在，请重新输入");
            }else{
                returnInfo = new ReturnInfo("false","验证通过");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo = new ReturnInfo("true","系统异常，请联系管理员");
        }
        return returnInfo;
    }
}
