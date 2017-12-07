package com.dch.service;

import com.dch.facade.MessageFacade;
import com.dch.facade.common.VO.Page;
import com.dch.vo.MessageRecVo;
import com.dch.vo.MessageSendDetail;
import com.dch.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */
@Path("cms/message")
@Controller
@Produces("application/json")
public class MessageService {

    @Autowired
    private MessageFacade messageFacade;

    /**
     * 添加，修改站内信
     * @param messageVo
     * @return
     */
    @POST
    @Path("merge")
    public Response mergeMessage(MessageVo messageVo){
      return  messageFacade.mergeMessage(messageVo);
    }

    /**
     * 根据站内信id删除站内信 为逻辑删除 status标记为-1
     * @param id
     * @return
     * @throws Exception
     */
    @POST
    @Path("delete")
    public Response deleteMessageText(@QueryParam("id")String id) throws Exception{
        return messageFacade.deleteMessageText(id);
    }

    /**
     * 根据用户id获取站内信
     * @param userId
     * @param title 站内信标题
     * @param perPage
     * @param currentPage
     * @return
     * @throws Exception
     */
    @GET
    @Path("get-self-create-messages")
    public Page<MessageVo> getCreateMessagesBySelf(@QueryParam("userId") String userId, @QueryParam("title")String title,@QueryParam("flag")String flag, @QueryParam("perPage")int perPage, @QueryParam("currentPage")
            int currentPage) throws Exception{
        return messageFacade.getCreateMessagesBySelf(userId,title,flag,perPage,currentPage);
    }

    /**
     * 根据站内信id获取收信人员信息
     * @param messageId
     * @return
     */
    @GET
    @Path("get-self-messageRecVos")
    public List<MessageRecVo> getSelfMessageRecVos(@QueryParam("messageId")String messageId,@QueryParam("flag")String flag, @QueryParam("perPage")
            int perPage, @QueryParam("currentPage")int currentPage){
        return messageFacade.getSelfMessageRecVos(messageId,flag,perPage,currentPage);
    }

    /**
     * 登录用户获取自己收到的站内信通知
     * @param userId
     * @param title
     * @return
     */
    @GET
    @Path("get-my-message-texts")
    public Page<MessageSendDetail> getMyMessageTexts(@QueryParam("userId")String userId, @QueryParam("title")String title,@QueryParam("flag")String flag, @QueryParam("perPage")
            int perPage, @QueryParam("currentPage")int currentPage){
        return messageFacade.getMyMessageTexts(userId,title,flag,perPage,currentPage);
    }

    /**
     * 根据站内信id，用户id，状态标识站内信为已读或删除状态
     * @param id
     * @param userId
     * @param status 0：未读 1:已读 -1：删除
     * @return
     */
    @POST
    @Path("change-message-status")
    public Response changeMessage(@QueryParam("id")String id,@QueryParam("userId")String userId,@QueryParam("status")String status){
        return messageFacade.changeMessage(id,userId,status);
    }
}
