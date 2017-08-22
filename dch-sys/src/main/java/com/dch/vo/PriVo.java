package com.dch.vo;

import com.dch.entity.ResourceOperation;
import com.dch.entity.Module;
import com.dch.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户在某个模块下拥有的菜单和操作
 * Created by Administrator on 2017/7/19.
 */
public class PriVo {

    private User user ;
    private Module module ;
    private List<MenusVo> menus = new ArrayList<MenusVo>() ;
    private List<ResourceOperation> resourceOperations = new ArrayList<ResourceOperation>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MenusVo> getMenus() {
        return menus;
    }

    public void setMenus(List<MenusVo> menus) {
        this.menus = menus;
    }

    public List<ResourceOperation> getResourceOperations() {
        return resourceOperations;
    }

    public void setResourceOperations(List<ResourceOperation> resourceOperations) {
        this.resourceOperations = resourceOperations;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
