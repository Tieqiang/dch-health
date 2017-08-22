package com.dch.entity.base;



import com.dch.util.UserUtils;
import com.dch.vo.UserVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/19.
 */
@MappedSuperclass
public class BaseEntity {


    private String id ;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private String createBy;
    private String modifyBy;
    private String status;

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
    @Column(name = "create_date",updatable = false)
    public Timestamp getCreateDate() {
        if(this.createDate==null){
            this.createDate =new Timestamp(new Date().getTime());
        }
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {

        this.createDate = createDate;
    }

    @Basic
    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        if(this.modifyDate==null){
            this.modifyDate =new Timestamp(new Date().getTime());
        }
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Basic
    @Column(name = "create_by",updatable = false)
    public String getCreateBy() {
        if(this.createBy==null||"".equals(createBy)){
            try {
                UserVo yunUsers = UserUtils.getCurrentUser();
                if(yunUsers!=null){
                    this.createBy = yunUsers.getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return createBy;
            }
        }
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "modify_by")
    public String getModifyBy() {
        UserVo yunUsers = UserUtils.getCurrentUser();
        return yunUsers.getId();
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
