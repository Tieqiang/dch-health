package com.dch.service;

import com.dch.entity.Technology;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

@Controller
@Path("front/technology")
@Produces("application/json")
public class TechnologyService {


    @Autowired
    private BaseFacade baseFacade ;


    @GET
    @Path("find-range")
    public Page<Technology> getTechnologyRange(@QueryParam("perPage") int perPage,@QueryParam("currentPage") int currentPage){
        Page<Technology> page = new Page<>();
        List<Technology> all = baseFacade.findAll(Technology.class);
        page.setPerPage((long) perPage);
        int size = all.size();
        page.setCounts((long) size);

        String hql = "from Technology order by publishTime desc ";
        TypedQuery<Technology> query = baseFacade.createQuery(Technology.class, hql, new ArrayList<Object>());
        query.setFirstResult(perPage*(currentPage-1));
        query.setMaxResults(perPage);
        List<Technology> resultList = query.getResultList();
        page.setData(resultList);
        return page ;
    }


    @GET
    @Path("get-technology-by-id")
    public Technology getTechnology(@QueryParam("id")String id){
        return baseFacade.get(Technology.class,id);
    }
}
