package com.dch.facade;

import com.dch.entity.MrSubject;
import com.dch.facade.common.BaseFacade;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class MrSubjectFacade extends BaseFacade {


    /**
     *添加、删除、修改病例
     * @param mrSubject
     * @return
     */
    @Transactional
    public Response mergeMrSubject(MrSubject mrSubject) {
        MrSubject merge = merge(mrSubject);
        return Response.status(Response.Status.OK).entity(merge).build();
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
            throw new Exception("不存在哦。。");
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
