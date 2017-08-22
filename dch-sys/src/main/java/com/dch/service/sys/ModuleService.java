package com.dch.service.sys;

import com.dch.entity.Module;
import com.dch.entity.User;
import com.dch.entity.UserVsModule;
import com.dch.facade.ModuleFacade;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.ReturnInfo;
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
@Path("sys/module")
@Produces("application/json")
public class ModuleService {

    @Autowired
    private ModuleFacade moduleFacade;

    /**
     * 获取模块列表
     * @param whereHql 拼接的hql语句 以and 开头
     * @return
     */
    @GET
    @Path("get-modules")
    public List<Module> getModules(@QueryParam("whereHql") String whereHql){
        return moduleFacade.getModules(whereHql);
    }

    /**
     *获取单个模块
     * @param moduleId 模块id
     * @param whereHql 扩展查询sql 以and 开头
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-module")
    public Module getModuel(@QueryParam("moduleId") String moduleId,@QueryParam("whereHql") String whereHql) throws Exception{
           return moduleFacade.getModuel(moduleId,whereHql);
    }

    /**
     * 新增、修改、删除模块 删除为逻辑删除 修改status状态为-1
     * @param module
     * @return
     */
    @POST
    @Transactional
    @Path("merge")
    public Response mergeModule(Module module){
        Module merge = moduleFacade.merge(module);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取模块用户信息
     * @param moduleId
     * @return
     */
    @GET
    @Path("get-module-users")
    public List<User> getModuleUsers(@QueryParam("moduleId") String moduleId){
        return moduleFacade.getModuleUsers(moduleId);
    }


    /**
     * 模块分配用户
     * @param moduleId 模块id
     * @param userList 用户对象数组
     * @return
     */
    @POST
    @Path("set-module-user")
    public Response setModuleUser(@QueryParam("moduleId") String moduleId,List<User> userList) throws Exception{
        moduleFacade.setModuleUser(moduleId,userList);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","模块分配用户")).build();
    }
}
