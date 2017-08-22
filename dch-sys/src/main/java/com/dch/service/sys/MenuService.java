package com.dch.service.sys;

import com.dch.entity.Menu;
import com.dch.facade.MenuFacade;
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
@Path("sys/menu")
@Produces("application/json")
public class MenuService{

    @Autowired
    private MenuFacade menuFacade ;

    /**
     * 获取菜单
     * @param moduleId
     * @return
     */
    @GET
    @Path("get-menus")
    public List<Menu> getMenus(@QueryParam("moduleId") String moduleId){
        return  menuFacade.getMenus(moduleId);
    }

    /**
     * 获取菜单
     * @param menuId
     * @return
     */
    @GET
    @Path("get-menu")
    public Menu getMenu(@QueryParam("menuId")String menuId){
        return menuFacade.get(Menu.class,menuId);
    }

    /**
     * 添加、删除、修改单个菜单
     * @param menu
     * @return
     */
    @Path("merge")
    @POST
    @Transactional
    public Response mergeMenu(Menu menu){
        Menu merge = menuFacade.merge(menu);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 删除菜单及子菜单
     * @param menuId
     * @return
     */
    @POST
    @Path("delete-menu")
    public Response deleteMenuAndChild(@QueryParam("menuId") String menuId) throws Exception{
        menuFacade.deleteMenuAndChild(menuId);
        return Response.status(Response.Status.OK).entity(new ReturnInfo("true","成功")).build();
    }

}
