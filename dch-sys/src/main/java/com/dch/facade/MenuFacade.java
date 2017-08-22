package com.dch.facade;

import com.dch.entity.Menu;
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
public class MenuFacade extends BaseFacade {
    /**
     * 获取菜单
     * @param moduleId
     * @return
     */
    public List<Menu> getMenus( String moduleId){
        String hql = "from Menu as menu where menu.status<>'-1'" ;
        if(moduleId!=null&&!"".equals(moduleId)){
            hql+="  and menu.moduleId='"+moduleId+"'";
        }
        return  createQuery(Menu.class,hql,new ArrayList<Object>()).getResultList();
    }


    /**
     * 删除菜单及子菜单
     * @param menuId
     * @return
     */
    @Transactional
    public void deleteMenuAndChild(String menuId) throws Exception{
        try {
            String updateHql = "update Menu as m set m.status='-1' where parentId='"+menuId+"'" ;
            excHql(updateHql);
            Menu menu = get(Menu.class,menuId);
            menu.setStatus("-1");
            merge(menu);
        } catch (Exception e) {
            throw e;
        }
    }
}
