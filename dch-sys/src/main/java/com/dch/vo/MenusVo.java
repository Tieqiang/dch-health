package com.dch.vo;

import com.dch.entity.FunctionMenu;
import com.dch.entity.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于角色管理和角色菜单管理
 *
 * Created by Administrator on 2017/7/31.
 */
public class MenusVo {
    private Menu menu ;
    private List<FunctionMenu> functionMenus = new ArrayList<FunctionMenu>() ;
    public MenusVo(Menu menu, List<FunctionMenu> functionMenus) {
        this.menu = menu;
        this.functionMenus = functionMenus;
    }

    public MenusVo() {
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<FunctionMenu> getFunctionMenus() {
        return functionMenus;
    }

    public void setFunctionMenus(List<FunctionMenu> functionMenus) {
        this.functionMenus = functionMenus;
    }
}
