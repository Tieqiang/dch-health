package com.dch.facade;

import com.dch.entity.DrugBaseInfo;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 药物基本信息维护
 * Created by Administrator on 2017/8/22.
 */
@Service
public class DrugBaseInfoFacade extends BaseFacade{

    @Transactional
    public DrugBaseInfo mergeDrugBaseInfo(DrugBaseInfo drugBaseInfo) throws Exception{
        if(StringUtils.isEmptyParam(drugBaseInfo.getId())){//添加
            String code = getDrugCodeBySequenc("drug_base_info");
            drugBaseInfo.setDrugCode(code);
        }
        return merge(drugBaseInfo);
    }

    public String getDrugCodeBySequenc(String tableName) throws Exception{
        String sql = "select seq('SEQ_"+tableName+"') from dual";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            Object[] params = (Object[])list.get(0);
            String value = params[0].toString();
            return completingCode(value);
        }else{
            throw new Exception("表"+tableName+"序列不存在");
        }
    }
    public String completingCode(String number){
        StringBuffer sb = new StringBuffer("");
        if(number!=null && number.length()<5){
            for(int i=0;i<5-number.length();i++){
                sb.append("0");
            }
        }
        sb.append(number);
        return sb.toString();
    }
}
