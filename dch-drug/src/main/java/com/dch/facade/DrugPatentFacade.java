package com.dch.facade;

import com.dch.entity.DrugPatent;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品专利文献维护
 * Created by Administrator on 2017/8/23.
 */
@Service
public class DrugPatentFacade extends BaseFacade{

    @Transactional
    public DrugPatent mergeDrugPatent(DrugPatent drugPatent) {
        return merge(drugPatent);
    }

    /**
     * 获取专利文献
     * @param patentName
     * @param wherehql
     * @return
     */
    public List<DrugPatent> getDrugPatents(String patentName, String wherehql) {
        String hql = "from DrugPatent where status<>'-1'";
        if(!StringUtils.isEmptyParam(patentName)){
            hql += " and patentName like '%"+patentName+"%'";
        }
        if(!StringUtils.isEmptyParam(wherehql)){
            hql += " and "+wherehql;
        }
        return createQuery(DrugPatent.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     *  获取单个专利文献
     * @param patentId
     * @return
     */
    public DrugPatent getDrugPatent(String patentId) throws Exception{
        String hql = "from DrugPatent where status<>'-1' and id = '"+patentId+"'";
        List<DrugPatent> drugPatentList = createQuery(DrugPatent.class,hql,new ArrayList<Object>()).getResultList();
        if(drugPatentList!=null && !drugPatentList.isEmpty()){
            return drugPatentList.get(0);
        }else{
            throw new Exception("专利文件信息不存在");
        }
    }
}
