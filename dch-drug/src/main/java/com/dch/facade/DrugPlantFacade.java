package com.dch.facade;

import com.dch.entity.DrugPlant;
import com.dch.entity.DrugPlantPicture;
import com.dch.facade.common.BaseFacade;
import com.dch.vo.DrugPlantVo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
     * @return
     */
    public List<DrugPlantVo> getDrugPlants(String name) {

        List<DrugPlantVo> drugPlantVos = new ArrayList<>();

        String hql = "from DrugPlant as p where p.status<>'-1'" ;
        if(name!=null&&!"".equals(name)){
            hql+=" and (p.nameCn like '"+name+"' or p.nameLatin like '%"+name+"%')";
        }
        List<DrugPlant> drugPlants = createQuery(DrugPlant.class,hql,new ArrayList<Object>()).getResultList();
        for(DrugPlant plant :drugPlants){
            List<DrugPlantPicture> drugPlantPictures= getDrugPlantPictures(plant.getId());
            DrugPlantVo drugPlantVo = new DrugPlantVo();
            drugPlantVo.setDrugPlant(plant);
            drugPlantVo.setDrugPlantPictures(drugPlantPictures);
            drugPlantVos.add(drugPlantVo);
        }
        return drugPlantVos;
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
