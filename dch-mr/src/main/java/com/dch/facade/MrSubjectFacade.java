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
     * @return
     * @throws Exception
     */
    public MrSubject getMrSubject(String subjectId) throws Exception {
        String hql=" from MrSubject where status <> '-1' and id = '"+subjectId+"'";
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
}
