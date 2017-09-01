package com.dch.facade;

import com.dch.entity.DrugPlant;
import com.dch.entity.DrugPlantPicture;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.DrugPlantVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Component
public class DrugPlantFacade extends BaseFacade {
    /**
     * 查询药用植物
     * @param name
     * @param perPage
     *@param currentPage @return
     */
    public Page<DrugPlantVo> getDrugPlants(String name, int perPage, int currentPage) {

        List<DrugPlantVo> drugPlantVos = new ArrayList<>();

        String hql = "from DrugPlant as p where p.status<>'-1'" ;
        String hqlCount = "select count(*) from DrugPlant as p where p.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql+=" and (p.nameCn like '"+name+"' or p.nameLatin like '%"+name+"%')";
            hqlCount+=" and (p.nameCn like '"+name+"' or p.nameLatin like '%"+name+"%')";
        }
        TypedQuery<DrugPlant> query = createQuery(DrugPlant.class, hql, new ArrayList<Object>());
        Long counts = createQuery(Long.class,hqlCount,new ArrayList<Object>()).getSingleResult();
        Page page =new Page();
        if(perPage<=0){
            perPage=20;
        }
        if (perPage > 0) {
            if(currentPage<=0){
                currentPage =1;
            }
            query.setFirstResult((currentPage-1) * perPage);
            query.setMaxResults(currentPage * perPage);
            page.setPerPage((long) perPage);
        }
        List<DrugPlant> drugPlantList = query.getResultList();

        for(DrugPlant plant :drugPlantList){
            List<DrugPlantPicture> drugPlantPictures= getDrugPlantPictures(plant.getId());
            DrugPlantVo drugPlantVo = new DrugPlantVo();
            drugPlantVo.setDrugPlant(plant);
            drugPlantVo.setDrugPlantPictures(drugPlantPictures);
            drugPlantVos.add(drugPlantVo);
        }
        page.setData(drugPlantVos);
        page.setCounts(counts);
        return page;
    }

    /**
     * 根据植物Id，获取所有的植物图片
     * @param id
     * @return
     */
    private List<DrugPlantPicture> getDrugPlantPictures(String id) {
        String hql = "from DrugPlantPicture as p where p.status<>'-1' and p.drugPlantId='"+id+"' " ;
        List<DrugPlantPicture> drugPlantPictures = createQuery(DrugPlantPicture.class, hql, new ArrayList<Object>()).getResultList();
        return drugPlantPictures;
    }

    /**
     * 获取具体的药用植物信息
     * @param id
     * @return
     * @throws Exception
     */
    public DrugPlantVo getDrugPlant(String id) throws Exception {
        DrugPlant plant = get(DrugPlant.class, id);
        String status = plant.getStatus();
        if("".equals(status)||"-1".equals(status)){
            throw new Exception("查询对象已经被删除，或者不存在");
        }
        List<DrugPlantPicture> drugPlantPictures = getDrugPlantPictures(plant.getId());
        DrugPlantVo drugPlantVo = new DrugPlantVo();
        drugPlantVo.setDrugPlant(plant);
        drugPlantVo.setDrugPlantPictures(drugPlantPictures);

        return drugPlantVo;
    }

    /**
     * 添加、删除、修改药用植物
     * @param drugPlantVo
     * @return
     */
    @Transactional
    public DrugPlantVo mergeDrugPlant(DrugPlantVo drugPlantVo) {
        DrugPlantVo drugPlantVo1  =new DrugPlantVo();
        DrugPlant drugPlant = drugPlantVo.getDrugPlant();
        drugPlant = merge(drugPlant);
        drugPlantVo1.setDrugPlant(drugPlant);
        List<DrugPlantPicture> drugPlantPictures = drugPlantVo.getDrugPlantPictures();
        for(DrugPlantPicture drugPlantPicture:drugPlantPictures){
            drugPlantPicture.setDrugPlantId(drugPlant.getId());
            drugPlantVo1.getDrugPlantPictures().add(merge(drugPlantPicture));
        }
        return drugPlantVo1;
    }
}
