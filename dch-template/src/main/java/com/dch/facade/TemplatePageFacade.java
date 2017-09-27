package com.dch.facade;

import com.dch.entity.TemplatePage;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class TemplatePageFacade extends BaseFacade {

    /**
     * 添加、删除、删除表单页
     * @param templatePage
     * @return
     */
    @Transactional
    public Response mergeTemplatePage(TemplatePage templatePage) {
        TemplatePage merge = merge(templatePage);
        return Response.status(Response.Status.OK).entity(merge).build();
    }

    /**
     * 获取表单页面
     * @param templateId
     * @return
     */
    public List<TemplatePage> getTemplatePages(String templateId) {
        String hql=" from TemplatePage where status <> '-1' and templateId = ' "+templateId+" '";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<>()).getResultList();
        return templatePageList;
    }

    /**
     * 获取具体的表单页
     * @param pageId
     * @return
     * @throws Exception
     */
    public TemplatePage getTemplatePage(String pageId) throws Exception {
        String hql=" from TemplatePage where status <> '-1' and id = ' "+pageId+" '";
        List<TemplatePage> templatePageList = createQuery(TemplatePage.class, hql, new ArrayList<>()).getResultList();
        if(templatePageList!=null &&templatePageList.size()>0){
            return templatePageList.get(0);
        }else{
            throw new Exception("该模板页面不存在");
        }
    }
}
