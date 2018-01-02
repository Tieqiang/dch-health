package com.dch.service;

import com.dch.entity.PolicyResources;
import com.dch.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunkqa on 2017/12/27.
 */
@Produces("application/json")
@Path("front/import")
@Controller
public class DataImportService {

    private Map map = new ConcurrentHashMap();
    private static  String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
    private static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";  //定义style的正则表达式
    @GET
    @Path("get-str-my")
    public void getString(){
        return;
    }

    @Autowired
    private BaseFacade baseFacade;

    public static Document getDocument (String url){
        try {

            return Jsoup.connect(url).get();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }
    public static List<Document> getDocuments(List<String> urls){
        List<Document> list = new ArrayList<>();
        for(String url:urls){
            Document doc = getDocument(url);
            list.add(doc);
        }
        return list;
    }

    public static String getHtmlSource(String url) {
        String linesep, htmlLine;
        linesep = System.getProperty("line.separator");

        StringBuffer htmlSource = new StringBuffer();
        try {
            java.net.URL source = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(source
                    .openStream(), "gb2312"));
            while ((htmlLine = in.readLine()) != null) {
                System.out.println(htmlLine);
                htmlSource.append(htmlLine + linesep);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlSource.toString();
    }

    @GET
    @Path("import-data")
    @Transactional
    public List<String> importDataByJson(@QueryParam("number") int number) throws Exception {
        //List<String> list = new ArrayList<>();
//        for(int i=1;i<1000;i++){
//            String path = "E:\\json";
//            path = path+"\\data-"+i+"页.json";
//            File file=new File(path);
//            if(file.exists()){
//                String content= FileUtils.readFileToString(file,"UTF-8");
//                List<Map> maps= (List) JSONArray.fromObject(content);
//                for(int k=0;k<maps.size();k++){
//                    Map mapk = maps.get(k);
//                    System.out.println(mapk);
//                }
//            }else{
//                list.add(path);
//            }
//        }
        //Document doc = t.getDocument("http://www.sda.gov.cn/WS01/CL1857/");
        List<String> urltotal = new ArrayList<>();
            //String url_range = "http://sousuo.gov.cn/s.htm?q=&n=30&p="+number+"&t=paper&advance=true&title=&content=&puborg=&pcodeJiguan=&pcodeYear=&pcodeNum=&childtype=&subchildtype=&filetype=&timetype=timeqb&mintime=&maxtime=&sort=&sortType=1&nocorrect=";
            //String url_range = "http://www.sda.gov.cn/WS01/CL1857/index_1.html";//http://www.sda.gov.cn/WS01/CL1857/index_1.html,http://www.sda.gov.cn/WS01/CL1857/
            String url_range = "http://www.sda.gov.cn/WS01/CL1857/index_1.html";
            List<String> urls = new ArrayList<>();
            urltotal.add(url_range);
            //urls.add("http://www.sda.gov.cn/WS01/CL1857/");
            //urls.add("http://www.sda.gov.cn/WS01/CL1857/index_1.html");
            //urls.add("http://www.sda.gov.cn/WS01/CL1842/");
            urls.add(url_range);
            //urls.add("http://www.sda.gov.cn/WS01/CL1842/index_2.html");
            List<Document> documents = getDocuments(urls);
            List<Map> list = new ArrayList<>();
            List<PolicyResources> policyResourcesList = new ArrayList<>();
            for(Document doc:documents){
                Elements elements1 = doc.select("[class=ListColumnClass15]");
                int size = elements1.size();
                if(size<1){
                    elements1 = doc.select("[class=res-list]");
                    size = elements1.size();
                }
                Elements es=elements1.select("a");
                for(int i=0;i<size;i++){
                    String hb = "";
                    String title = elements1.get(i).text();
                    String url = es.get(i).attr("href");
                    if(url.contains("..")){
                        url = url.replace("..","http://www.sda.gov.cn/WS01");
                    }
                    try {
                        Document docIn = getDocument(url);
                        Elements elements3 = docIn.select("[class=wrap]");
                        if(elements3.size()==0){
                            elements3 = docIn.select("table");
                        }
                        elements3.select("[class=de_bannerS]").remove();
                        elements3.select("[id=frame_content]").remove();
                        elements3.select("[id=qr_container]").remove();
                        String content = elements3.toString();
//                        content = content.replace("border:1px","");
//                        content = content.replace("border-right:1px","");
//
//                        content = content.replace("../../../72.files/openpage.gif","http://www.gov.cn/zhengce/130.files/openpage.gif");
//                        content = content.replace("../../../72.files/close.gif","http://www.gov.cn/zhengce/130.files/close.gif");
//                        content = content.replace("../../../72.files/save.gif","http://www.gov.cn/zhengce/130.files/save.gif");
//
//                        content = "<style type=\"text/css\"><!--.bd1 td {\tline-height: 20px;\tpadding-left: 5px;}.bd1 {\tfont-size: 12px;}--></style>"+content;
                        Map map = new HashMap();
                        map.put("title",title);
                        map.put("content",content);
                        //System.out.println(content);
                        System.out.println("==================================================================================");
                        PolicyResources policyResources = new PolicyResources();
                        policyResources.setTitle(title);
                        policyResources.setTypeName("医疗器械法律法规");
                        policyResources.setContent(content);
                        policyResourcesList.add(policyResources);
                    }catch (Exception e){

                    }
                    list.add(map);
                }
            }
        mergePolicyResources(policyResourcesList);
        return urltotal;
    }

    @Transactional
    public Response mergePolicyResources(List<PolicyResources> policyResourcesList){
        baseFacade.batchInsert(policyResourcesList);
        return Response.status(Response.Status.OK).entity(policyResourcesList.get(0)).build();
    }
}
