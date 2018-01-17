package com.dch.service;

import com.dch.facade.TemplatePageConfigFacade;
import com.dch.util.HtmlToPdf;
import com.dch.util.LogHome;
import com.dch.vo.TemplatePageConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/1/15.
 */
@Produces("application/json")
@Path("template/template-page-config")
@Controller
public class TemplatePageConfigService {

    @Autowired
    private TemplatePageConfigFacade templatePageConfigFacade;

    /**
     * 保存表单页数据信息
     * @param templatePageConfigVo
     * @return
     */
    @POST
    @Path("merge-template-page-config")
    public Response mergeTemplatePageConfig(TemplatePageConfigVo templatePageConfigVo){
        return templatePageConfigFacade.mergeTemplatePageConfig(templatePageConfigVo);
    }

    @GET
    @Path("get-template-page-config-by-id")
    public TemplatePageConfigVo getTemplatePageConfigVoById(@QueryParam("pageId")String pageId){
        return templatePageConfigFacade.getTemplatePageConfigVoById(pageId);
    }

    @GET
    @Path("html-to-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downLoadFile(@QueryParam("templateId")String templateId, @QueryParam("htmlContent")String htmlContent) throws IOException {
        final String pPath = "F:\\logs";
        String fileTitle = "页面导出pdf";
        try {
            HtmlToPdf.convert(pPath+"\\test.html", pPath+"\\test.pdf");
        }catch (Exception e){
            e.printStackTrace();
        }
        StreamingOutput streamingOutput = new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                FileInputStream fileInputStream = new FileInputStream(pPath+"/test.pdf");
                int length =  0 ;
                byte[] bytes = new byte[1024];
                while(-1!=(length=fileInputStream.read(bytes))){
                    outputStream.write(bytes);
                }
                outputStream.flush();
                outputStream.close();
                fileInputStream.close();
            }
        };
        String fileName = URLEncoder.encode(fileTitle, "UTF-8");
        LogHome.getLog().info(fileName);
        return Response.status(Response.Status.OK).entity(streamingOutput).header("Content-disposition","attachment;filename="+ fileName)
                .header("Cache-Control","no-cache").build();
    }
}
