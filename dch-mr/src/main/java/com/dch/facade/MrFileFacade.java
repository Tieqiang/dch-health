package com.dch.facade;

import com.dch.entity.MrFile;
import com.dch.entity.MrSubject;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class MrFileFacade extends BaseFacade {

    @Autowired
    private BaseSolrFacade baseSolrFacade;

    /**
     * 添加、删除、修改病例内容
     * @param mrFile
     * @return
     */
    @Transactional
    public MrFile mergeMrFileContent(MrFile mrFile) throws Exception {
        if(!"-1".equals(mrFile.getStatus())){
            if(StringUtils.isEmptyParam(mrFile.getFileTitle())){
                throw new Exception("病例标题内容不能为空！");
            }
        }
        MrFile merge = merge(mrFile);
        SolrVo solrVo=new SolrVo();
        solrVo.setTitle(merge.getFileTitle());
        solrVo.setDesc(merge.getFileContent());
        solrVo.setCategory(merge.getSubjectCode());
        solrVo.setId(merge.getId());
        solrVo.setLabel(merge.getKeyWords());
        solrVo.setCategoryCode(merge.getSubjectCode());
        baseSolrFacade.addObjectMessageToMq(solrVo);
        return merge;
    }

    /**
     * 查询文章内容
     * @param fileId
     * @return
     * @throws Exception
     */
    public MrFile getMrFile(String fileId) throws Exception {
        String hql=" from MrFile where status <> '-1' and id = '"+fileId+"'";
        List<MrFile> mrFileList = createQuery(MrFile.class, hql, new ArrayList<>()).getResultList();
        if(mrFileList!=null&& !mrFileList.isEmpty()){
            return mrFileList.get(0);
        }else{
            throw new Exception("该查询的文章内容不存在。。");
        }
    }

    /**
     * 获取知识库列表
     * @param subjectCode
     * @param title
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<MrFile> getMrFiles(String subjectCode, String title, int perPage, int currentPage) {
        String hql=" from MrFile where status <> '-1' ";
        String hqlCount="select count(*) from MrFile where status<> '-1' ";
        if(subjectCode!=null&&!"".equals(subjectCode)){
            hql+="and subjectCode='"+ subjectCode +"' ";
            hqlCount+="and subjectCode='"+ subjectCode +"' ";
        }
        if(title!=null&&!"".equals(title)){
            hql+="and fileTitle like '%"+title+"%'";
            hqlCount+="and fileTitle like '%"+title+"%'";
        }
        TypedQuery<MrFile> query = createQuery(MrFile.class, hql, new ArrayList<>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20 ;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage=1 ;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(perPage);
            page.setPerPage((long) perPage);
        }
        List<MrFile> mrFileList = query.getResultList();
        page.setCounts(counts);
        page.setData(mrFileList);
        return page;
    }

    /**
     * 根据项目获取病例数据
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<MrFile> getMrFilesByProjectId(String projectId, int perPage, int currentPage) {
        String hql=" from MrFile where status <> '-1' and projectId='"+projectId+"'";
        Page<MrFile> mrFilePage = getPageResult(MrFile.class, hql, perPage, currentPage);
        return mrFilePage;
    }
}
