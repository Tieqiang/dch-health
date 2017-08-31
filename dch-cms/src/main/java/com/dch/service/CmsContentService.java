package com.dch.service;

import com.dch.entity.CmsCategory;
import com.dch.entity.CmsContent;
import com.dch.entity.CmsContentLabel;
import com.dch.facade.CmsContentFacade;
import com.dch.facade.common.VO.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static javax.ws.rs.core.Response.Status.OK;

/**
 * Created by Administrator on 2017/8/23.
 */
@Path("cms/content")
@Controller
@Produces("application/json")
public class CmsContentService {

    @Autowired
    private CmsContentFacade cmsContentFacade ;


    /**
     * 添加、删除、修改内容信息
     * @param cmsContent
     * @return
     */
    @POST
    @Path("merge-content")
    @Transactional
    public CmsContent mergeContent(CmsContent cmsContent){
        return cmsContentFacade.merge(cmsContent);
    }

    /**
     * 获取新闻列表
     * @param perPage         每页显示条数
     * @param currentPage     当前页
     * @param whereHql        前端传递的其他条件
     * @param title           标题，进行模糊匹配
     * @param categoryId      分类
     * @param startTime       时间范围创建时间大于等于这个时间
     * @param stopTime        时间范围创建时间小于等于这个时间
     * @return
     */
    @GET
    @Path("get-contents")
    public Page<CmsContent> getContents(@QueryParam("perPage") int perPage,
                                        @QueryParam("currentPage")int currentPage,
                                        @QueryParam("wherehql")String whereHql,
                                        @QueryParam("title")String title,
                                        @QueryParam("categoryId")String categoryId,
                                        @QueryParam("startTime")Timestamp startTime,
                                        @QueryParam("stopTime")Timestamp stopTime,
                                        @QueryParam("pubStatus")String pubStatus){
        return cmsContentFacade.getContents(perPage,currentPage,whereHql,title,categoryId,startTime,stopTime,pubStatus);
    }


    /**
     * 获取单一的内容信息
     * @param contentId
     * @return
     */
    @GET
    @Path("get-content")
    public CmsContent getContent(@QueryParam("contentId")String contentId){
        return cmsContentFacade.getContent(contentId);
    }




    @POST
    @Path("set-content-labels")
    public List<CmsContentLabel> setContentLables(@QueryParam("contentId") String contentId,List<String> labelNames) throws Exception {
        return cmsContentFacade.setContentLabels(contentId,labelNames);
    }


    /**
     * 根据标签获取
     * @param labelName
     * @param perPage
     * @param currentPage
     * @return
     */
    @GET
    @Path("get-contents-by-label")
    public Page<CmsContent> getContentsByLabel(@QueryParam("labelName")String labelName,
                                               @QueryParam("perPage")int perPage,
                                               @QueryParam("currentPage")int currentPage,
                                               @QueryParam("categoryId")String categoryId){
        return cmsContentFacade.getContentByLabel(labelName,perPage,currentPage,categoryId);
    }

    /**
     * 获取新闻标签
     * @param contentId
     * @return
     */
    @GET
    @Path("get-content-labels")
    public List<CmsContentLabel> getContentLabels(@QueryParam("contentId") String contentId){
        return cmsContentFacade.getContentLabels(contentId);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFiles(@FormDataParam( "file") InputStream uploadedInputStream,
                                @FormDataParam( "file") FormDataContentDisposition fileDetail,
                                @Context ServletContext context) throws Exception {
//
        String filename =fileDetail.getFileName();
        String path = context.getRealPath("/");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String exName = filename.substring(filename.lastIndexOf('.'));
        String filePath = "/upload";
        if(month>=10){
            path = path+"/upload"+"/"+year+month+day+"/"+hour+"/"+filename;
            filePath =filePath+"/"+year+month+day+"/"+hour+"/"+filename;
        }else{
            path = path+"/upload"+"/"+year+"0"+month+day+"/"+hour+"/"+filename;
            filePath =filePath+"/"+year+"0"+month+day+"/"+hour+"/"+filename;
        }



        File file= new File(path);
        if(!file.exists()){

            File fileParent = file.getParentFile() ;
            if(!fileParent.exists()){
                if(!fileParent.mkdirs()){
                    throw new Exception("创建文件路径失败");
                };
            }
            if(!file.createNewFile()){
                throw new Exception("创建文件失败!");
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int length = 0 ;
        while(-1 !=(length=uploadedInputStream.read(bytes))){
            fileOutputStream.write(bytes);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        return Response.status(Response.Status.OK).entity(filePath).build();

    }

}
