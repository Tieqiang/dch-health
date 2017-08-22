package com.dch.facade;

import com.dch.entity.Module;
import com.dch.entity.User;
import com.dch.entity.UserVsModule;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
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
public class ModuleFacade extends BaseFacade {
    /**
     * 获取模块列表
     * @param whereHql 拼接的hql语句 以and 开头
     * @return
     */
    public List<Module> getModules(String whereHql){
        String hql = " from Module where status<> '-1' ";
        if(whereHql!=null && !"".equals(whereHql)){
            hql += whereHql;
        }
        return createQuery(Module.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *获取单个模块
     * @param moduleId 模块id
     * @param whereHql 扩展查询sql 以and 开头
     * @return
     * @throws Exception
     */
    public Module getModuel(String moduleId,String whereHql) throws Exception{
        String hql = " from Module where status<> '-1' and id = '"+moduleId+"' ";
        if(whereHql!=null && !"".equals(whereHql)){
            hql += whereHql;
        }
        List<Module> moduleList = createQuery(Module.class,hql,new ArrayList<Object>()).getResultList();
        if(moduleList!=null && !moduleList.isEmpty()){
            return moduleList.get(0);
        }else{
            throw new Exception("模块信息不存在");
        }
    }

    /**
     * 获取模块用户信息
     * @param moduleId
     * @return
     */
    public List<User> getModuleUsers(String moduleId){
        String hql = "select user from User as user,UserVsModule as uvm where uvm.userId = user.id " +
                " and user.status<> '-1' and uvm.moduleId = '"+moduleId+"'";
        return createQuery(User.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 模块分配用户
     * @param moduleId 模块id
     * @param userList 用户对象数组
     * @return
     */
    @Transactional
    public void setModuleUser(String moduleId,List<User> userList) throws Exception{
        try {
            for(User user:userList){
                String hql = "select count(*) from UserVsModule where moduleId = '"+moduleId+"' and userId ='"+user.getId()+"'";
                Long num = createQuery(Long.class,hql,new ArrayList<Object>()).getSingleResult();
                if(num<1){
                    UserVsModule userVsModule = new UserVsModule();
                    userVsModule.setUserId(user.getId());
                    userVsModule.setModuleId(moduleId);
                    merge(userVsModule);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
