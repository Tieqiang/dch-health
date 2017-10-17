package org.wltea.analyzer.utils;

/**
 * Created by Administrator on 2017/9/24.
 */
public class StringUtils {
    public static boolean isNotBlank(String parm){
        if(parm!=null && !"".equals(parm)){
            return true;
        }
        return false;
    }
}
