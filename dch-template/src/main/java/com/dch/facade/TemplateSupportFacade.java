package com.dch.facade;

import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

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
}
