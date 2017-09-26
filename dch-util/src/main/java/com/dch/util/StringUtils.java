package com.dch.util;


import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
    // 定义HTML标签的正则表达式
    private static String regEx_html = "<[^>]+>";
    // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    private static String regEx_special = "\\&[a-zA-Z]{1,10};";

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     *
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String toUnderScoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线分隔的字符串转换为驼峰式。如果转换前的字符串为空，则返回null</br>
     * 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toHumpName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return null;
        } else if (!name.contains("_")) {
            // 不含下划线，仅将字母小写
            return name.toLowerCase();
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 标准的年龄的现实方式
     *
     * @param birthDay
     * @return
     */
    public static String getStandardAgeFromBirthday(Date birthDay) {
        if (birthDay != null) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTime(new Date());
            calendar2.setTime(birthDay);
            Integer currentYear = calendar1.get(Calendar.YEAR);
            Integer currentMonth = calendar1.get(Calendar.MONTH);
            Integer currentDay = calendar1.get(Calendar.DAY_OF_MONTH);

            Integer myYear = currentYear - calendar2.get(Calendar.YEAR);
            Integer myMonth = currentMonth - calendar2.get(Calendar.MONTH);
            Integer myDay = currentDay - calendar2.get(Calendar.DAY_OF_MONTH);
            String s = "";

            if (myYear < 6) {
                if (myYear < 1) {
                    if (myMonth < 1) {
                        if (myDay < 1) {
                            s = 1 + "天";
                        } else {
                            s = myDay + "天";
                        }
                    } else {
                        s = myMonth + "月" + myDay + "天";
                    }
                } else {
                    s = myYear + "岁" + myMonth + "月";
                }
            } else {
                s = myYear + "岁";
            }
            return s;
        } else {
            return "";
        }
    }

    /**
     * InputStream转换为byte[]
     *
     * @param is
     * @return byte[]
     */
    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    public static Boolean isEmptyParam(String input){
        Boolean isEmpty = true;
        if(input!=null && !"".equals(input)){
            isEmpty = false;
        }
        return isEmpty;
    }

    public static String remeveHtmlLabel(String input){
        if(isEmptyParam(input)){
            return input;
        }
        String htmlStr = input; // 含html标签的字符串
        String textStr = "";
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(htmlStr);
        htmlStr = m_special.replaceAll(""); // 过滤特殊标签

        Pattern p = Pattern.compile("\t|\r|\n");
        Matcher m = p.matcher(htmlStr);
        htmlStr = m.replaceAll("");
        htmlStr = htmlStr.replaceAll("[\\pP\\pS]", "");
        htmlStr = htmlStr.replace(":","");
        htmlStr = htmlStr.replace("：","");
        textStr = htmlStr;
        textStr = textStr.replaceAll(" +"," ");
        return textStr;
    }

}
