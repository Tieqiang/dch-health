package com.dch.vo;

import com.dch.entity.FutureTarget;
import com.dch.entity.TargetLevel;

import java.util.List;

/**
 * Created by sunkqa on 2018/6/14.
 */
public class CurrentFutureVo {
    private List<TargetLevel> targetLevelList;
    private List<FutureTarget> futureTargetList;

    public List<TargetLevel> getTargetLevelList() {
        return targetLevelList;
    }

    public void setTargetLevelList(List<TargetLevel> targetLevelList) {
        this.targetLevelList = targetLevelList;
    }

    public List<FutureTarget> getFutureTargetList() {
        return futureTargetList;
    }

    public void setFutureTargetList(List<FutureTarget> futureTargetList) {
        this.futureTargetList = futureTargetList;
    }
}
