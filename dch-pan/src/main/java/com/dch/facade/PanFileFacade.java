package com.dch.facade;

import com.dch.entity.PanFile;
import com.dch.entity.PanFileCategory;
import com.dch.entity.PanFileStore;
import com.dch.facade.common.BaseFacade;
import com.dch.facade.common.VO.Page;
import com.dch.util.StringUtils;
import com.dch.util.UserUtils;
import com.dch.vo.UserVo;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2017/8/14.
 */
@Component
public class PanFileFacade extends BaseFacade {

    /**
     * 创建、删除、修改、移动所有文件夹
     * @param panFile
     * @return
     */
    @Transactional
    public PanFile mergePanFile(PanFile panFile) throws Exception{
        //首先判断是否为删除
        if(panFile.getStatus().equals("-1")){
            String hql="update PanFile set status='-1' where parent_id = '"+panFile.getId()+"' or id='"+panFile.getId()+"'";
            int statecode=excHql(hql);
            if (statecode>0){
                return panFile;
            }else{
                return new PanFile();
            }
        }else{
            String proHql = " from PanFile where status <> '-1' and fileTitle = '"+panFile.getFileTitle()+"' and id <> '"+panFile.getId()+"'" +
                    " and projectId = '"+panFile.getProjectId()+"'";
            proHql += " and createBy = '"+UserUtils.getCurrentUser().getId()+"'";
            List<PanFile> proPanFiles = createQuery(PanFile.class,proHql,new ArrayList<Object>()).getResultList();
            if(proPanFiles!=null && !proPanFiles.isEmpty()){
                throw new Exception("项目下文件名已存在，请修改");
            }

            String hql = " from PanFile where status<>'-1' and fileTitle = '"+panFile.getFileTitle()+"' and id <> '"+panFile.getId()+"'";
            if(panFile.getParentId()==null||panFile.getParentId().equals("")){
                hql +=" and parentId is null";
            }else{
                hql +=" and parentId = '"+panFile.getParentId()+"'";
            }
            hql += " and createBy = '"+UserUtils.getCurrentUser().getId()+"'";
            List<PanFile> panFiles = createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
            if(panFiles!=null && !panFiles.isEmpty()){
                throw new Exception("文件名已存在，请修改");
            }
           return merge(panFile);
        }
    }


    /**
     * 获取所有的文件夹资源
     * folder_flag: 如果不传递，则获取所有的文件，1表示文件夹，0表示文件。
     * parentId:    选传，如果传递则获取该文件夹下所有的文件及文件夹。
     * fileOwner:   文件拥有者
     * 如果都不传递，默认获取所有文件
     * @param folder_flag
     * @param parentId
     * @param fileOwner
     * @return
     */
    public List<PanFile> getPanFiles(String folder_flag,String parentId,String fileOwner){
        String hql="from PanFile where status <> '-1'";
        if (!StringUtils.isEmptyParam(folder_flag)){
            hql+=" and folderFlag = '"+folder_flag+"'";
        }
        if (!StringUtils.isEmptyParam(parentId)){
            hql+=" and parentId = '"+parentId+"'";
        }
        if (!StringUtils.isEmptyParam(fileOwner)){
            hql+=" and fileOwner = '"+fileOwner+"'";
        }
        List<PanFile> result = createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
        return result;
    }

    /**
     * 获取一级目录
     * @param fileOwner
     * @return
     */
    public List<PanFile> getPanFirstFolders(String fileOwner){
        String hql="from PanFile where status <> '-1' and fileOwner = '"+fileOwner+"' and parentId is null";
        return createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据文件类型获取所有文件
     * typeId:      类型ID
     * fileOwner:   文件拥有者，不传递则为当前登录用户
     * @param typeId
     * @param fileOwner
     * @return
     */
    public List<PanFile> getPanFilesByType(String typeId,String fileOwner){
        String hql="select distinct p from PanFile as p where p.status<>'-1'";
        if (!StringUtils.isEmptyParam(typeId)){
            hql+=" and p.fileTypeId = '"+typeId+"'";
        }
        if (!StringUtils.isEmptyParam(fileOwner)){
            hql+=" and p.fileOwner = '"+fileOwner+"'";
        }
        return createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据文件分类获取所有文件
     * 如果分类有子分类，则获取该分类及子分类下的所有文件
     * categoryId:分类
     * fileOwner:文件拥有者，不传递则为当前登录用户
     * @param categoryId
     * @param fileOwner
     * @return
     */
    public List<PanFile> getPanFilesByCategory(String categoryId,String fileOwner){
        String hql="select distinct p from PanFile as p where p.status <> '-1' ";
        if (!StringUtils.isEmptyParam(categoryId)){
            //首先查询该分类是否包含子分类
            String checkSonHql="select distinct p from PanFileCategory as p where p.status <> '-1'  and (p.parentCategoryId = '"+categoryId+"' or p.id = '"+categoryId+"')";
            List<PanFileCategory> resultList=createQuery(PanFileCategory.class,checkSonHql,new ArrayList<Object>()).getResultList();
            if (resultList.size()>1) {//如果该分类包含子类，获取该分类及子分类下的所有文件
                hql+=" and p.categoryId in (";
                ListIterator<PanFileCategory> li=resultList.listIterator();
                while (li.hasNext()){
                    PanFileCategory panFileCategory=li.next();
                    if (li.hasNext()==false){
                        hql+="'"+panFileCategory.getId()+"')";
                    }else {
                        hql+="'"+panFileCategory.getId()+"',";
                    }
                }
            }else {
                hql += " and p.categoryId = '" + categoryId+"'";
            }
        }
        if (!StringUtils.isEmptyParam(fileOwner)){
            hql+=" and p.fileOwner = '"+fileOwner+"'";
        }
        return createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
    }

    /**
     * 根据一组id值获取对应的PanFile对象
     * @param ids
     * @return
     */
    public List<PanFile> getPanFilesByIdArray(List<String> ids){
        String hql="select distinct p from PanFile as p where status<>'-1' and id in(";
        Iterator<String> li=ids.iterator();
        while (li.hasNext()){
            String id =li.next();
            if (li.hasNext()==false){
                hql+="'"+id+"')";
            }else{
                hql+="'"+id+"',";
            }
        }
        return createQuery(PanFile.class,hql,new ArrayList<Object>()).getResultList();
    }

    public PanFile getPanFileById(String id){
        return get(PanFile.class,id);
    }

    /**
     *文件(夹)批量移动，删除，设为共享，取消共享
     * @param ids       需要操作的文件接口
     * @param fileShare 文件共享标志
     * @param status    文件状态
     * @param parentId  父路径
     * @return
     */
    @Transactional
    public void mergePanFiles(List<String> ids, String fileShare, String status, String parentId) {
        for(String id :ids){
            PanFile panFile = get(PanFile.class,id);
            if(panFile!=null){
                if(!"".equals(fileShare)&&fileShare!=null){
                    panFile.setFileShare(fileShare);
                }
                if(!"".equals(status)&&status!=null){
                    panFile.setStatus(status);
                }
                if(!"".equals(parentId)&&parentId!=null){
                    panFile.setParentId(parentId);
                }
                merge(panFile);
            }
        }
    }

    /**
     * 文件上传
     * @param uploadedInputStream
     * @param fileDetail
     * @return
     * @throws Exception
     */
    @Transactional
    public PanFileStore uploadPanFileStore(InputStream uploadedInputStream, FormDataContentDisposition fileDetail,String fileSystem,String basePath) throws Exception {

        String filename  = fileDetail.getFileName() ;
        filename = new String(filename.getBytes("ISO-8859-1"),"utf-8");
        filename = URLDecoder.decode(filename,"UTF-8");
        String dbFileName = filename;
        String prefix = filename.substring(filename.lastIndexOf("."));
        String fileOtherName = filename.substring(0,filename.lastIndexOf("."));
        filename = fileOtherName+"-"+System.currentTimeMillis()+prefix;
        if(fileSystem==null||"".equals(fileSystem)){
            fileSystem = "local" ;
        }

        if(basePath==null||"".equals(basePath)){
            basePath =System.getProperty("user.dir");//获取当前项目路径
        }
        if("local".equals(fileSystem)){

            UserVo currentUser = UserUtils.getCurrentUser();
            if(currentUser==null){
                throw new Exception("获取当前用户信息为空");
            }
            String loginName = currentUser.getLoginName();
            String path = basePath+"\\"+loginName+"\\" ;
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int secode = c.get(Calendar.SECOND);

            path += year;
            path += (month<10?("0"+month):month)+""+(day<10?("0"+day):day);
            path += (hour<10?("0"+hour):hour)+""+(minute<10?("0"+minute):minute)+""+(secode<10?("0"+secode):secode);
            path+="\\"+filename;
            File file = new File(path) ;
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

            PanFileStore panFileStore = new PanFileStore();
            panFileStore.setFileName(dbFileName);
            panFileStore.setStorePath(path);
            return merge(panFileStore);
        }



        return null;
    }

    /**
     * 根据项目id获取项目数据文件
     * @param projectId
     * @param perPage
     * @param currentPage
     * @return
     */
    public Page<PanFile> getPanFilesByProjectId(String projectId, int perPage, int currentPage) {
        String hql=" from PanFile where status <> '-1' and projectId='"+projectId+"' ";
        Page<PanFile> panFilePage = getPageResult(PanFile.class, hql, perPage, currentPage);
        return panFilePage;
    }
}
