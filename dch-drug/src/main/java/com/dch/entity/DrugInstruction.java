package com.dch.entity;

import com.dch.entity.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/8/22.
 * 药品说明书
 */
@Entity
@Table(name = "drug_instruction", schema = "dch", catalog = "")
public class DrugInstruction extends BaseEntity {
    private String drugId;
    private String title;
    private String component;
    private String drugCharacter;
    private String useType;
    private String functions;
    private String dose;
    private String sepc;
    private String contraindication;
    private String untowardEffect;
    private String attentionMatters;
    private String drugInteraction;
    private String drugAction;
    private String storage;
    private Date period;
    private String useStandard;
    private String memo;
    private String drugName ;
    private String firmId ;

    private String packageInfo;


    @Basic
    @Column(name = "drug_id", nullable = true, length = 64)
    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "component", nullable = true, length = -1)
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Basic
    @Column(name = "drug_character", nullable = true, length = -1)
    public String getDrugCharacter() {
        return drugCharacter;
    }

    public void setDrugCharacter(String drugCharacter) {
        this.drugCharacter = drugCharacter;
    }

    @Basic
    @Column(name = "use_type", nullable = true, length = -1)
    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    @Basic
    @Column(name = "functions", nullable = true, length = -1)
    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    @Basic
    @Column(name = "dose", nullable = true, length = -1)
    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    @Basic
    @Column(name = "sepc", nullable = true, length = 200)
    public String getSepc() {
        return sepc;
    }

    public void setSepc(String sepc) {
        this.sepc = sepc;
    }

    @Basic
    @Column(name = "contraindication", nullable = true, length = -1)
    public String getContraindication() {
        return contraindication;
    }

    public void setContraindication(String contraindication) {
        this.contraindication = contraindication;
    }

    @Basic
    @Column(name = "untoward_effect", nullable = true, length = -1)
    public String getUntowardEffect() {
        return untowardEffect;
    }

    public void setUntowardEffect(String untowardEffect) {
        this.untowardEffect = untowardEffect;
    }

    @Basic
    @Column(name = "attention_matters", nullable = true, length = -1)
    public String getAttentionMatters() {
        return attentionMatters;
    }

    public void setAttentionMatters(String attentionMatters) {
        this.attentionMatters = attentionMatters;
    }

    @Basic
    @Column(name = "drug_interaction", nullable = true, length = -1)
    public String getDrugInteraction() {
        return drugInteraction;
    }

    public void setDrugInteraction(String drugInteraction) {
        this.drugInteraction = drugInteraction;
    }

    @Basic
    @Column(name = "drug_action", nullable = true, length = -1)
    public String getDrugAction() {
        return drugAction;
    }

    public void setDrugAction(String drugAction) {
        this.drugAction = drugAction;
    }

    @Basic
    @Column(name = "storage", nullable = true, length = -1)
    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Basic
    @Column(name = "period", nullable = true)
    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    @Basic
    @Column(name = "use_standard", nullable = true, length = 200)
    public String getUseStandard() {
        return useStandard;
    }

    public void setUseStandard(String useStandard) {
        this.useStandard = useStandard;
    }

    @Basic
    @Column(name = "memo", nullable = true, length = -1)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    @Basic
    @Column(name = "package_info", nullable = true, length = 20)
    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Column(name="drug_name")
    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    @Column(name="firm_id")
    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }
}
