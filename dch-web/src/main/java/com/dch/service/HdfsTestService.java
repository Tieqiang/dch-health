package com.dch.service;

import com.dch.facade.common.VO.ReturnInfo;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
//import org.apache.hadoop.fs.LocatedFileStatus;
//import org.apache.hadoop.fs.RemoteIterator;
//import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
//@Produces("application/json")
//@Path("hdfs")
//@Controller
public class HdfsTestService {

//    @Autowired
//    private  HdfsService hdfsService ;
//
//    @Context
//    private HttpServletResponse response ;
//    @GET
//    @Path("exist")
//    public ReturnInfo fileExist(@QueryParam("path") String path) throws IOException {
//        boolean exits = hdfsService.exits(path);
//        if(exits){
//            return new ReturnInfo("true","文件存在");
//        }else{
//            return new ReturnInfo("false","文件不存在");
//        }
//    }
//
//    @GET
//    @Path("list-path")
//    public List<LocatedFileStatus> listFile(@QueryParam("path")String path) throws IOException {
//        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = hdfsService.listFiles(path);
//        List<LocatedFileStatus> locatedFileStatuses = new ArrayList<LocatedFileStatus>();
//        while(locatedFileStatusRemoteIterator.hasNext()){
//            LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
//            locatedFileStatuses.add(next);
//        }
//        return locatedFileStatuses;
//    }
//
//
//    @GET
//    @Path("down-load")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response downLoadFile(@QueryParam("path") String path) throws IOException {
//        final byte[] readFile = hdfsService.readFile(path);
//        File file = new File(path);
//        String name = file.getName();
//        System.out.println(name);
//        String str = new String(readFile);
//        System.out.println(str);
//
//        StreamingOutput streamingOutput = new StreamingOutput() {
//            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//                outputStream.write(readFile);
//                outputStream.flush();
//            }
//        };
//        return Response.status(Response.Status.OK).entity(streamingOutput).header("Content-disposition","attachment;filename="+name)
//                .header("Cache-Control","no-cache").build();
//    }



}

