package com.dch.facade.common.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Page<T>{
    private List<T> data=new ArrayList<>() ;
    private Long perPage ;
    private Long counts ;//所有数据

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long counts) {
        this.counts = counts;
    }

    public Long getPerPage() {
        return perPage;
    }

    public void setPerPage(Long perPage) {
        this.perPage = perPage;
    }
}
