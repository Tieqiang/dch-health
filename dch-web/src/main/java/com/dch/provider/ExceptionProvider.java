package com.dch.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Created by heren on 2016/10/23.
 */
@Provider
public class ExceptionProvider implements ExceptionMapper<Exception> {

    @Context
    private HttpServletRequest httpServletRequest ;

    @Context
    private HttpServletResponse response ;


    @Produces("text/html")
    public Response toResponse(Exception exception) {

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
    }
}
