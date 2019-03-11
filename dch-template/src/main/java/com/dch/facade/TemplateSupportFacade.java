package com.dch.facade;

import com.dch.entity.TemplateResultSupport;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunkqa on 2018/3/19.
 */
@Component
public class TemplateSupportFacade extends BaseFacade {

    /**
     * 根据表单id获取文件名称和路径
     * @param masterId
     * @return
     */
    public Map<String,String> getFileStorePath(String masterId) {
        Map<String,String> map = new HashMap<>();
        String sql = "select p.store_path,p.file_name from template_result_support as s,pan_file_store as p where s.related_file_id = p.id and s.status<>'-1' and s.related_master_id = '"+masterId+"'";
        List list = createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            for(int i=0;i<list.size();i++){
                Object[] params = (Object[])list.get(i);
                map.put("store_path",params[0]+"");
                map.put("file_name",params[1]+"");
            }
        }
        return map;
    }

    /**
     * 根据表单id查询上传表单保存信息
     * @param masterId
     * @return
     * @throws Exception
     */
    public TemplateResultSupport getTemplateResultSupport(String masterId) throws Exception{
        if(StringUtils.isEmptyParam(masterId)){
            throw new Exception("填报表单不存在！");
        }else {
            String hql = " from TemplateResultSupport where relatedMasterId = '"+masterId+"'";
            return createQuery(TemplateResultSupport.class,hql,new ArrayList<>()).getSingleResult();
        }
    }
}
