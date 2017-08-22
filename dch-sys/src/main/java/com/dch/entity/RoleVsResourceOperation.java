package com.dch.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@Entity
@Table(name = "role_vs_resource_operation", schema = "dch", catalog = "")
public class RoleVsResourceOperation {
    private String id;
    private String roleId;
    private String resourceOperationId;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_id")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "resource_operation_id")
    public String getResourceOperationId() {
        return resourceOperationId;
    }

    public void setResourceOperationId(String resourceOperationId) {
        this.resourceOperationId = resourceOperationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleVsResourceOperation that = (RoleVsResourceOperation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (resourceOperationId != null ? !resourceOperationId.equals(that.resourceOperationId) : that.resourceOperationId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (resourceOperationId != null ? resourceOperationId.hashCode() : 0);
        return result;
    }
}
