package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by sunkqa on 2018/3/8.
 */
@Entity
@Table(name = "template_task", schema = "dch", catalog = "")
public class TemplateTask extends BaseEntity {
    private String taskName;
    private String taskType;
    private long taskCycle;//任务执行周期
    private String relatedTemplateMasterId;//关联的表单id
    private String isRun;//定时任务是否在运行 0:否 1:是
    private Date startTime;
    private Date endTime;

    @Basic
    @Column(name = "task_name", nullable = true, length = 200)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Basic
    @Column(name = "task_type", nullable = true, length = 10)
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Basic
    @Column(name = "task_cycle", nullable = false, length = 10)
    public long getTaskCycle() {
        return taskCycle;
    }

    public void setTaskCycle(long taskCycle) {
        this.taskCycle = taskCycle;
    }

    @Basic
    @Column(name = "related_template_masterId", nullable = true, length = 64)
    public String getRelatedTemplateMasterId() {
        return relatedTemplateMasterId;
    }

    public void setRelatedTemplateMasterId(String relatedTemplateMasterId) {
        this.relatedTemplateMasterId = relatedTemplateMasterId;
    }

    @Basic
    @Column(name = "is_run", nullable = true, length = 2)
    public String getIsRun() {
        return isRun;
    }

    public void setIsRun(String isRun) {
        this.isRun = isRun;
    }

    @Basic
    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
