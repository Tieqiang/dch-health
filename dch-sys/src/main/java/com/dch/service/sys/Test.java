package com.dch.service.sys;

import com.dch.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/17.
 */
@Controller
@Path("test")
@Produces("application/json")
public class Test {

    @Autowired
    private BaseFacade baseFacade  ;

    @GET
    @Path("time")
    public String get(){
        return String.valueOf(new Date().getTime());
    }
}
