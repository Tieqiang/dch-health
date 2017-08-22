package com.dch.service;

import com.dch.entity.PanFile;
import com.dch.facade.PanFileFacade;
import com.dch.facade.common.VO.ReturnInfo;
import com.dch.util.UserUtils;
import com.dch.util.StringUtils;
import com.dch.vo.UserVo;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;

/**
 * Created by Administrator on 2017/8/14.
 */
@Produces("application/json")
@Path("pan/file")
@Controller
public class PanFileService {


    @Autowired
    private PanFileFacade panFileFacade ;


    /**
     * 创建、删除、修改、移动所有文件夹
     * 如果删除的情况下，需要将该文件下所有关联的文件及文件夹全部删除
     * @param panFile
     * @return
     */
    @POST
    @Path("merge-pan-file")
    public Response mergePanFile(PanFile panFile) throws Exception{
       return Response.status(OK).entity(panFileFacade.mergePanFile(panFile)).build();
    }

    /**
     * 获取文件的具体信息
     * @param id
     * @return
     */
    @Path("get-pan-file")
    @GET
    public PanFile getPanFile(@QueryParam("fileId") String id){
        return panFileFacade.get(PanFile.class,id);
    }


    /**
     * 获取所有的文件夹资源
     * @param folder_flag   如果不传递，则获取所有的文件，1表示文件夹，0表示文件。
     * @param parentId      选传，如果传递则获取该文件夹下所有的文件及文件夹，
     * @param fileOwner     文件拥有者，不传递，则为当前登录用户
     * @return
     */
    @GET
    @Path("get-pan-files")
    public List<PanFile> getPanFiles(@QueryParam("folder_flag") String folder_flag, @QueryParam("parentId") String parentId,
                                     @QueryParam("fileOwner") String fileOwner) throws Exception {
        if (StringUtils.isEmptyParam(fileOwner)){
           UserVo user= UserUtils.getCurrentUser();
           fileOwner=user.getId();
        }
        return panFileFacade.getPanFiles(folder_flag,parentId,fileOwner);
    }

    /**
     * 获取一级目录
     * @param fileOwner 文件拥有者，不传递则为当前登录用户
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-first-files")
    public List<PanFile> getPanFirstFolders(@QueryParam("fileOwner") String fileOwner) throws Exception {
        if (StringUtils.isEmptyParam(fileOwner)){
            UserVo user= UserUtils.getCurrentUser();
            fileOwner=user.getId();
        }
        return panFileFacade.getPanFirstFolders(fileOwner);
    }

    /**
     * 根据文件类型获取所有文件
     * @param typeId        类型ID
     * @param fileOwner     文件拥有者，不传递则为当前登录用户
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-files-by-type")
    public List<PanFile> getPanFilesByType(@QueryParam("typeId") String typeId,@QueryParam("fileOwner") String fileOwner) throws Exception {
        if (StringUtils.isEmptyParam(fileOwner)){
            UserVo user= UserUtils.getCurrentUser();
            fileOwner=user.getId();
        }
        return panFileFacade.getPanFilesByType(typeId,fileOwner);
    }

    /**
     * 根据文件分类获取所有文件
     * 如果分类有子分类，则获取该分类及子分类下的所有文件
     * @param categoryId        分类
     * @param fileOwner         文件拥有者，不传递则为当前登录用户
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-files-by-category")
    public List<PanFile> getPanFilesByCategory(@QueryParam("categoryId") String categoryId,@QueryParam("fileOwner") String fileOwner) throws Exception {
        if (StringUtils.isEmptyParam(fileOwner)){
            UserVo user= UserUtils.getCurrentUser();
            fileOwner=user.getId();
        }
        return panFileFacade.getPanFilesByCategory(categoryId,fileOwner);
    }

    /**
     *
     * @param uploadedInputStream
     * @param fileDetail
     * @param request
     * @return
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("upload")
    @Transactional
    public Response uploadFiles(@FormDataParam( "file") InputStream uploadedInputStream,
                                @FormDataParam( "file") FormDataContentDisposition fileDetail, @Context HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");
        String filename=URLDecoder.decode(fileDetail.getFileName(),"UTF-8");
        String dir=System.getProperty("user.dir");//获取当前项目路径
        String path = dir+"\\uploadfile\\"+filename;//直接存放到项目下的uploadfile目录下
        File file= new File(path);
        String exName = filename.substring(filename.lastIndexOf('.'));
        System.out.println(exName);
        //创建文件夹目录
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
        //创建文件
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int length = 0 ;
        while(-1 !=(length=uploadedInputStream.read(bytes))){
            fileOutputStream.write(bytes);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        //增添PanFile的数据库记录
        PanFile panFile=new PanFile();
        panFile.setFileName(filename);
        panFile.setStatus("1");
        panFile.setStorePath(path);
        panFile=panFileFacade.mergePanFile(panFile);
        return Response.status(OK).entity(panFile).build();
    }


    /**
     *文件(夹)批量移动，删除，设为共享，取消共享
     * @param ids       需要操作的文件接口
     * @param fileShare 文件共享标志
     * @param status    文件状态
     * @param parentId  父路径
     * @return
     */
    @POST
    @Path("merge-pan-files")
    public Response mergePanFiles(List<String> ids,@QueryParam("fileShare")String fileShare,@QueryParam("status")String status,
                                  @QueryParam("parentId")String parentId){
        panFileFacade.mergePanFiles(ids,fileShare,status,parentId);
        return Response.status(OK).entity(new ReturnInfo("true","修改成功")).build();
    }
}