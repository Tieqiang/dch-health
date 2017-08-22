package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@Entity
@Table(name = "module", schema = "dch", catalog = "")
public class Module extends BaseEntity{
    private String moduleName;
    private String description;
    private String icon;
    private String indexHref;


    @Basic
    @Column(name = "module_name")
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "index_href")
    public String getIndexHref() {
        return indexHref;
    }

    public void setIndexHref(String indexHref) {
        this.indexHref = indexHref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        if (moduleName != null ? !moduleName.equals(module.moduleName) : module.moduleName != null) return false;
        if (description != null ? !description.equals(module.description) : module.description != null) return false;
        if (icon != null ? !icon.equals(module.icon) : module.icon != null) return false;
        if (indexHref != null ? !indexHref.equals(module.indexHref) : module.indexHref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (moduleName != null ? moduleName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (indexHref != null ? indexHref.hashCode() : 0);

        return result;
    }
}
