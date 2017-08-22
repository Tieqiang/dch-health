package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@Entity
@Table(name = "menu", schema = "dch", catalog = "")
public class Menu extends BaseEntity {
    private String menuName;
    private String path;
    private String viewPath;
    private String parentId;
    private String icon;
    private String moduleId;
    private String menuCode ;
    private Integer rank;
    private Integer orderNum;


    @Basic
    @Column(name = "menu_name")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "view_path")
    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    @Basic
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "module_id")
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Basic
    @Column(name = "rank")
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Basic
    @Column(name = "order_num")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Basic
    @Column(name="menu_code")
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (menuName != null ? !menuName.equals(menu.menuName) : menu.menuName != null) return false;
        if (path != null ? !path.equals(menu.path) : menu.path != null) return false;
        if (viewPath != null ? !viewPath.equals(menu.viewPath) : menu.viewPath != null) return false;
        if (parentId != null ? !parentId.equals(menu.parentId) : menu.parentId != null) return false;
        if (icon != null ? !icon.equals(menu.icon) : menu.icon != null) return false;
        if (moduleId != null ? !moduleId.equals(menu.moduleId) : menu.moduleId != null) return false;
        if (rank != null ? !rank.equals(menu.rank) : menu.rank != null) return false;
        if (orderNum != null ? !orderNum.equals(menu.orderNum) : menu.orderNum != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (viewPath != null ? viewPath.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (moduleId != null ? moduleId.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + (orderNum != null ? orderNum.hashCode() : 0);

        return result;
    }

}
