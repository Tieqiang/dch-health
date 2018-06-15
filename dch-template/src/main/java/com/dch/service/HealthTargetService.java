package com.dch.service;

import com.dch.entity.FutureTarget;
import com.dch.entity.HealthTarget;
import com.dch.entity.TargetLevel;
import com.dch.facade.HealthTargetFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.vo.CurrentFutureVo;
import com.dch.vo.HealthTargetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkqa on 2018/6/14.
 */
@Controller
@Produces("application/json")
@Path("health/target")
public class HealthTargetService {

    @Autowired
    private HealthTargetFacade healthTargetFacade;

    /**
     * 根据id获取指标信息
     * @param id
     * @return
     */
    @GET
    @Path("get-health-target")
    public Response getHealthTarget(@QueryParam("id")String id){
        HealthTargetVo healthTargetVo = new HealthTargetVo();
        HealthTarget healthTarget = healthTargetFacade.get(HealthTarget.class,id);
        healthTargetVo.setName(healthTarget.getName());
        healthTargetVo.setParentId(healthTarget.getParentId());
        healthTargetVo.setComputeMethod(healthTarget.getComputeMethod());
        healthTargetVo.setDependFile(healthTarget.getDependFile());
        healthTargetVo.setGrade(healthTarget.getGrade());
        healthTargetVo.setOtherCountryStep(healthTarget.getOtherCountryStep());
        healthTargetVo.setOtherCountryValue(healthTarget.getOtherCountryValue());
        healthTargetVo.setStep(healthTarget.getStep());
        healthTargetVo.setSimilarCountry(healthTarget.getSimilarCountry());
        healthTargetVo.setStatus(healthTarget.getStatus());
        List<TargetLevel> targetLevelList = healthTargetFacade.getTargetLevelList(id);
        List<FutureTarget> futureTargetList = healthTargetFacade.getFutureTargetList(id);
        healthTargetVo.setTargetLevelList(targetLevelList);
        healthTargetVo.setFutureTargetList(futureTargetList);
        return Response.status(Response.Status.OK).entity(healthTargetVo).build();
    }
    /**
     * 新增，修改指标
     * @param healthTarget
     * @return
     */
    @POST
    @Path("merge")
    @Transactional
    public Response merge(HealthTarget healthTarget){
        HealthTarget merge = healthTargetFacade.merge(healthTarget);
        return Response.status(Response.Status.OK).entity(merge).build();
    }
//    /**
//     * 新增，修改指标
//     * @param healthTargetVo
//     * @return
//     */
//    @POST
//    @Path("merge")
//    @Transactional
//    public Response merge(HealthTargetVo healthTargetVo){
//        HealthTarget merge = healthTargetFacade.saveHealthTarget(healthTargetVo);
//        if(!"-1".equals(healthTargetVo.getStatus())){//不为-1说明是新增或修改
//            if(!StringUtils.isEmptyParam(healthTargetVo.getId())){//如果id不为空为修改，则先删除指标项 再保存
//                healthTargetFacade.deleteTargetLevel(merge.getId());
//                healthTargetFacade.deleteFutureTarget(merge.getId());
//            }
//            if(healthTargetVo.getTargetLevelList()!=null && !healthTargetVo.getTargetLevelList().isEmpty()){
//                for(TargetLevel targetLevel:healthTargetVo.getTargetLevelList()){
//                    targetLevel.setRelatedHealthTarget(merge.getId());
//                }
//                healthTargetFacade.batchInsert(healthTargetVo.getTargetLevelList());
//            }
//            if(healthTargetVo.getFutureTargetList()!=null && !healthTargetVo.getFutureTargetList().isEmpty()){
//                for(FutureTarget futureTarget:healthTargetVo.getFutureTargetList()){
//                    futureTarget.setRelatedHealthTarget(merge.getId());
//                }
//                healthTargetFacade.batchInsert(healthTargetVo.getFutureTargetList());
//            }
//        }else{
//            healthTargetFacade.deleteTargetLevel(merge.getId());
//            healthTargetFacade.deleteFutureTarget(merge.getId());
//        }
//        return Response.status(Response.Status.OK).entity(healthTargetVo).build();
//    }

    /**
     * 根据指标id删除指标信息
     * @param id
     * @return
     */
    @POST
    @Path("delete")
    @Transactional
    public Response delete(@QueryParam("id")String id) throws Exception{
        HealthTarget healthTarget = healthTargetFacade.get(HealthTarget.class,id);
        List<HealthTarget> healthTargetList = getHealthTargetList(id);
        if(healthTargetList!=null && !healthTargetList.isEmpty()){
            throw new Exception("该指标项包含子指标，请先删除根节点指标项");
        }
        healthTarget.setStatus("-1");
        HealthTarget merge =healthTargetFacade.merge(healthTarget);
        healthTargetFacade.deleteTargetLevel(healthTarget.getId());
        healthTargetFacade.deleteFutureTarget(healthTarget.getId());
        return Response.status(Response.Status.OK).entity(merge).build();
    }
    /**
     * 根据id获取其包含的子指标，如果id为空 查询一级指标进行展示
     * @param id
     * @return
     */
    @GET
    @Path("get-health-target-list")
    public List<HealthTarget> getHealthTargetList(@QueryParam("id")String id){
        String hql = " from HealthTarget where status<>'-1'";
        if(StringUtils.isEmptyParam(id)){
            hql += " and grade = 1";
        }else {
            hql += " and parentId = '"+id+"'";
        }
        List<HealthTarget> healthTargetList = healthTargetFacade.createQuery(HealthTarget.class,hql,new ArrayList<Object>()).getResultList();
        return healthTargetList;
    }

    /**
     * 获取所有的指标信息
     * @param id
     * @return
     */
    @GET
    @Path("get-all-health-target")
    public List<HealthTarget> getAllHealthTargetList(@QueryParam("id")String id){
        String hql = " from HealthTarget where status<>'-1'";
        if(!StringUtils.isEmptyParam(id)){
            hql += " and parentId = '"+id+"'";
        }
        List<HealthTarget> healthTargetList = healthTargetFacade.createQuery(HealthTarget.class,hql,new ArrayList<Object>()).getResultList();
        return healthTargetList;
    }
    /**
     * 根据指标id获取其目前水平值及目标值
     * @param id
     * @return
     */
    @GET
    @Path("get-target-value")
    public Response getTargetValueById(@QueryParam("id")String id){
        CurrentFutureVo currentFutureVo = new CurrentFutureVo();
        List<TargetLevel> targetLevelList = healthTargetFacade.getTargetLevelList(id);
        List<FutureTarget> futureTargetList = healthTargetFacade.getFutureTargetList(id);
        currentFutureVo.setTargetLevelList(targetLevelList);
        currentFutureVo.setFutureTargetList(futureTargetList);
        return Response.status(Response.Status.OK).entity(currentFutureVo).build();
    }

    /**
     * 获取所有健康中国指标信息
     * @param name
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-health-target-page")
    public Page<HealthTarget> getHealthTargetPage(@QueryParam("name")String name,@QueryParam("perPage")int perPage,
                                                  @QueryParam("currentPage")int currentPage){
        return healthTargetFacade.getHealthTargetPage(name,perPage,currentPage);
    }

    /**
     * 根据指标id获取指标项值
     * @param id
     * @return
     */
    @GET
    @Path("get-target-level-list")
    public List<TargetLevel> getTargetLevelList(@QueryParam("id")String id){
        List<TargetLevel> list = healthTargetFacade.getTargetLevelList(id);
        return list;
    }

    /**
     * 根据指标id查询目标值信息
     * @param id
     * @return
     */
    @GET
    @Path("get-future-target-list")
    public List<FutureTarget> getFutureTargetList(@QueryParam("id")String id){
        List<FutureTarget> list = healthTargetFacade.getFutureTargetList(id);
        return list;
    }

    /**
     * 新增修改指标值
     * @param targetLevel
     * @return
     */
    @POST
    @Path("merge-target-level")
    @Transactional
    public Response mergeTargetLevel(TargetLevel targetLevel){
        return Response.status(Response.Status.OK).entity(healthTargetFacade.merge(targetLevel)).build();
    }

    /**
     * 根据指标id删除指标值
     * @param id
     * @return
     */
    @POST
    @Path("delete-target-level")
    @Transactional
    public Response deleteTargetLevel(@QueryParam("id")String id){
        List list = new ArrayList();
        //healthTargetFacade.deleteTargetLevel(id);
        list.add(id);
        healthTargetFacade.removeByStringIds(TargetLevel.class,list);
        return Response.status(Response.Status.OK).entity(list).build();
    }
    /**
     * 新增修改目标值
     * @param futureTarget
     * @return
     */
    @POST
    @Path("merge-future-target")
    @Transactional
    public Response mergeFutureTarget(FutureTarget futureTarget){
        return Response.status(Response.Status.OK).entity(healthTargetFacade.merge(futureTarget)).build();
    }

    /**
     * 根据指标id删除目标值
     * @param id
     * @return
     */
    @POST
    @Path("delete-future-target")
    @Transactional
    public Response deleteFutureTarget(@QueryParam("id")String id){
        List list = new ArrayList();
        //healthTargetFacade.deleteTargetLevel(id);
        list.add(id);
        healthTargetFacade.removeByStringIds(FutureTarget.class,list);
        return Response.status(Response.Status.OK).entity(list).build();
    }
}
