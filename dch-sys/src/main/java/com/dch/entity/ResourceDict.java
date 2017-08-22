package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@Entity
@Table(name = "resource_dict", schema = "dch", catalog = "")
public class ResourceDict extends BaseEntity {
    private String moduleId;
    private String resourceName;



    @Basic
    @Column(name = "module_id")
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Basic
    @Column(name = "resource_name")
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName =resourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceDict that = (ResourceDict) o;

        if (moduleId != null ? !moduleId.equals(that.moduleId) : that.moduleId != null) return false;
        if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (moduleId != null ? moduleId.hashCode() : 0);
        result = 31 * result + (resourceName != null ? resourceName.hashCode() : 0);
        return result;
    }
}
