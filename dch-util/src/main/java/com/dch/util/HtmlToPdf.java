package com.dch.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2018/1/16.
 */
public class HtmlToPdf {
    /**
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String srcPath, String destPath){
        //wkhtmltopdf在系统中的路径
        String str="";
        try {
            URL url = DBUtils.class.getClassLoader().getResource("wkhtmltopdf-0.8.3.exe");
            str = URLDecoder.decode(url.getFile(), "utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String path = new File(str).getPath();
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(path);
        cmd.append(" ");
        cmd.append("--page-size A4") ;
        cmd.append("");//页眉下面的线
        cmd.append(" ");//页眉中间内容
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);

        boolean result = true;
        try{
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String args[]){
        convert("F:\\logs\\test1.html","F:\\logs\\test1.pdf");
    }
}
