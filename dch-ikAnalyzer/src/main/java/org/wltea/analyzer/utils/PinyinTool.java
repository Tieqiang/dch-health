package org.wltea.analyzer.utils;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Administrator on 2017/9/24.
 */
public class PinyinTool {
    static HanyuPinyinOutputFormat format = null;
    public static enum Type {
        UPPERCASE,              //全部大写
        LOWERCASE,              //全部小写
        FIRSTUPPER              //首字母大写
    }
    static{
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public PinyinTool(){
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static String toPinYin(String str){
        return toPinYin(str, "", Type.UPPERCASE);
    }

    public static String toPinYin(String str,String spera){
        return toPinYin(str, spera, Type.UPPERCASE);
    }

    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * 如： 明天 转换成 MINGTIAN
     * @param str
     * @param spera
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toPinYin(String str, String spera, Type type) {
        if(str == null || str.trim().length()==0)
            return "";
        if(type == Type.UPPERCASE)
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        else
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        String py = "";
        String temp = "";
        String[] t=null;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if((int)c <= 128)
                py += c;
            else{
                try {
                    t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                if(t == null)
                    py += c;
                else{
                    temp = t[0];
                    if(type == Type.FIRSTUPPER)
                        temp = t[0].toUpperCase().charAt(0)+temp.substring(1);
                    py += temp+(i==str.length()-1?"":spera);
                }
            }
        }
        return py.trim();
    }
}
