package com.dch.entity;

import com.dch.entity.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@Entity
@Table(name = "resource_operation", schema = "dch", catalog = "")
public class ResourceOperation extends BaseEntity{
    private String resourceDictId;
    private String operationCode;
    private String resourceOperationDesc ;
    private String resourceOperationName ;




    @Basic
    @Column(name = "resource_dict_id")
    public String getResourceDictId() {
        return resourceDictId;
    }

    public void setResourceDictId(String resourceDictId) {
        this.resourceDictId = resourceDictId;
    }

    @Basic
    @Column(name = "operation_code")
    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    @Basic
    @Column(name="resource_operation_desc")
    public String getResourceOperationDesc() {
        return resourceOperationDesc;
    }

    public void setResourceOperationDesc(String resourceOperationDesc) {
        this.resourceOperationDesc = resourceOperationDesc;
    }

    @Basic
    @Column(name="resource_operation_name")
    public String getResourceOperationName() {
        return resourceOperationName;
    }

    public void setResourceOperationName(String resourceOperationName) {
        this.resourceOperationName = resourceOperationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceOperation that = (ResourceOperation) o;

        if (resourceDictId != null ? !resourceDictId.equals(that.resourceDictId) : that.resourceDictId != null) return false;
        if (operationCode != null ? !operationCode.equals(that.operationCode) : that.operationCode != null)
            return false;
        if (resourceOperationDesc != null ? !resourceOperationDesc.equals(that.resourceOperationDesc) : that.resourceOperationDesc != null) return false;
        if (resourceOperationName != null ? !resourceOperationName.equals(that.resourceOperationName) : that.resourceOperationName != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result =  0;
        result = 31 * result + (resourceDictId != null ? resourceDictId.hashCode() : 0);
        result = 31 * result + (operationCode != null ? operationCode.hashCode() : 0);
        result = 31 * result + (resourceOperationDesc != null ? resourceOperationDesc.hashCode() : 0);
        result = 31 * result + (resourceOperationName != null ? resourceOperationName.hashCode() : 0);
        return result;
    }
}
