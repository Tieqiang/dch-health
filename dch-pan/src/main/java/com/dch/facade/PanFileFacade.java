package com.dch.facade;

import com.dch.entity.PanFile;
import com.dch.entity.PanFileCategory;
import com.dch.facade.common.BaseFacade;
import com.dch.util.StringUtils;
import org.hibernate.metamodel.domain.PluralAttributeNature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
            String hql = " from PanFile where status<>'-1' and fileName = '"+panFile.getFileName()+"' and id <> '"+panFile.getId()+"'";
            if(panFile.getParentId()==null||panFile.getParentId().equals("")){
                hql +=" and parentId is null";
            }else{
                hql +=" and parentId = '"+panFile.getParentId()+"'";
            }
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
}
