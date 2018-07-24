package com.dch.test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sunkqa on 2018/3/6.
 */
public class VeryLogin {
    private static final long serialVersionUID = 244554953219893949L;
    private final static String captcha_id = GeetestConfig.getGeetest_id();
    private final static String private_key = GeetestConfig.getGeetest_key();
    public static boolean Verify(HttpServletRequest request) throws ServletException, IOException {

        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(), GeetestConfig.isnewfailback());

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        //从session中获取userid
        String userid = request.getSession()+(String)request.getSession().getAttribute("userid");

        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("user_id",userid);
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, hashMap);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证

            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            return true;
        }
        else {
            // 验证失败
            return false;
        }
    }
}
