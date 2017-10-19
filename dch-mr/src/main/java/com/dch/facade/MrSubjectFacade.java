package com.dch.facade;

import com.dch.entity.MrSubject;
import com.dch.facade.common.BaseFacade;
import com.dch.util.PinYin2Abbreviation;
import com.dch.util.StringUtils;
import com.dch.vo.SolrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class MrSubjectFacade extends BaseFacade {

    @Autowired
    private BaseSolrFacade baseSolrFacade;


    /**
     *添加、删除、修改病例
     * @param mrSubject
     * @return
     */
    @Transactional
    public MrSubject mergeMrSubject(MrSubject mrSubject) {
        MrSubject merge = merge(mrSubject);
        SolrVo solrVo=new SolrVo();
        solrVo.setTitle(merge.getSubjectName());
        solrVo.setDesc(merge.getSubjectName()+","+merge.getSubjectCode());
        solrVo.setCategory(merge.getSubjectName());
        solrVo.setId(merge.getId());
        solrVo.setLabel(merge.getSubjectName());
        solrVo.setCategoryCode(merge.getSubjectCode());
        baseSolrFacade.addObjectMessageToMq(solrVo);
        return merge;
    }

    /**
     *查询学科信息
     * @return
     */
    public List<MrSubject> getMrSubjects() {
        String hql=" from MrSubject where status <> '-1' ";
        return createQuery(MrSubject.class,hql,new ArrayList<>()).getResultList();
    }
    /**
     *获取具体的学科信息
     * @param subjectId
     * @param subjectCode
     * @return
     * @throws Exception
     */
    public MrSubject getMrSubject(String subjectId, String subjectCode) throws Exception {
        String hql=" from MrSubject where status <> '-1' ";
        if(null!=subjectId&&!"".equals(subjectId)){
            hql+="and id = '"+subjectId+"'";
        }
        if(null!=subjectCode&&!"".equals(subjectCode)){
            hql+=" and subjectCode='"+subjectCode+"' ";
        }
        List<MrSubject> subjectList = createQuery(MrSubject.class, hql, new ArrayList<>()).getResultList();
        if(subjectList!=null&& !subjectList.isEmpty()){
            return subjectList.get(0);
        }else{
            throw new Exception("该查询的学科信息不存在。。");
        }
    }

    /**
     *获取学科的子学科信息
     * @param subjectId
     * @return
     */
    public List<MrSubject> getSubMrSubjects(String subjectId) {
        String hql=" from MrSubject where status <> '-1' and parentSubjectId = '"+subjectId+"'";
        return createQuery(MrSubject.class,hql,new ArrayList<>()).getResultList();
    }

    /**
     *根据项目id获取学科分类信息
     * @param projectId
     * @return
     */
    public List<MrSubject> getMrSubjectsByprojectId(String projectId) {
        String hql = " from MrSubject as m where m.status<> '-1' and m.subjectCode in (select subjectCode from " +
                " MrFile where status<>'-1' and projectId = '"+projectId+"')";
        List<MrSubject> mrSubjects = createQuery(MrSubject.class, hql, new ArrayList<Object>()).getResultList();
        List<MrSubject> mrSubjectList = mrSubjects;
        String parentIds = "";
        do{
            parentIds = "";
            for(MrSubject m:mrSubjectList){
                if(!StringUtils.isEmptyParam(m.getParentSubjectId()) && !parentIds.contains(m.getParentSubjectId())){
                    parentIds = ",'"+m.getParentSubjectId()+"'";
                }
            }
            if(!StringUtils.isEmptyParam(parentIds)){
                parentIds = parentIds.substring(1);
                mrSubjectList = getMrSubjectByParentIds(parentIds);
                mrSubjects.addAll(mrSubjectList);
            }
        }while (!StringUtils.isEmptyParam(parentIds));
        return mrSubjects;
    }

    private List<MrSubject> getMrSubjectByParentIds(String parentIds) {
        String hql = "from MrSubject where status<> '-1' and id in ("+parentIds+")";
        return createQuery(MrSubject.class, hql, new ArrayList<Object>()).getResultList();
    }
}

