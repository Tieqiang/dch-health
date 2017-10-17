package org.wltea.analyzer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 高亮工具类
 *
 *Created by Administrator on 2017/9/24.
 *
 * 功能描述：
 *        1.普通高亮
 *        2.同音高亮
 *        3.汉字拼音混合高亮
 *        4.自动合并
 *        5.智能截取
 */
public class HighlightUtil {
    public static void main(String[] args) {
        // String content = "2015年被市场称为国企改革的“落地年”。市场时常会失控，所以要时常把握市场。";
//        String content="姓名：张三  拼音：zhangsan";
//        String keyword[]="zhangsan".split(" ");
//        String content = "市场时常会失控，所以要时常把握市场。";
//        String keyword[] = "市场 suoyi".split(" ");

        String content = "打dota的孙康庆，经常能打赢。";
        String keyword[] = "sunkangqing ying".split(" ");
        System.out.println(contentHighlight(content, keyword, true, 0));
    }

    /**
     * 检索内容高亮
     *
     * @param content
     *            内容
     * @param keywords
     *            搜索关键词
     * @param isHomophony
     *            是否同音
     * @param maxLength
     *            最大长度（0为不限制）
     * @return
     */
    public static String contentHighlight(String content, String keywords[],
                                          boolean isHomophony, int maxLength) {
        String cutContent = "";// 参照未截取内容
        boolean isCut = false;
        // 1.根据最大长度截取内容
        if (maxLength > 0) {
            if (StringUtils.isNotBlank(content) && content.length() > maxLength) {
                cutContent = content.substring(0, maxLength);
                isCut = true;
            }
        }
        try {
            List<Postion> list = new ArrayList<Postion>();// 所有匹配下标位置的集合
            // 2.遍历关键词-匹配所有关键词下标位置
            for (int i = 0; i < keywords.length; i++) {
                if (StringUtils.isNotBlank(keywords[i].trim())) {
                    list.addAll(getAllIndex(content, keywords[i], isHomophony));
                }
            }
            // 3.下标集合-并集处理
            if (isCut) {// 是否被截取
                //如果文本内容已被截取，更新关键词在文本中正确的结束下标位置
                for (int i = 0; i < list.size();) {
                    if (list.get(i).start > maxLength - 1) {//如果关键词的开始位置大于截取长度
                        list.remove(i);//移除无效下标位置
                    } else {
                        //如果关键词开始下标在截取范围内，结束下标在截取范围外，将结束下标改为文本最大下标
                        if (list.get(i).end > maxLength - 1)
                            list.get(i).end = maxLength - 1;
                        i++;
                    }
                }
            }
            // 3.1 合并下标位置
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    list.get(i).union(list.get(j));
                }
            }
            // 3.2 去除被合并的下标位置
            for (int i = 0; i < list.size();) {
                if (list.get(i).isUnion()) {
                    list.remove(i);
                } else {
                    i++;
                }
            }
            // 4.替换文本标签-关键词高亮
            // 4.1 按下标开始位置从小到大排序
            Collections.sort(list, new Comparator<Postion>() {
                public int compare(Postion arg0, Postion arg1) {
                    return arg0.start - arg1.start;
                }
            });

            // 4.2给文本中关键词添加高亮标红的闭合标签
            String font = "<font color='red'>";
            String font2 = "</font>";
            if (isCut)
                content = cutContent;
            for (int i = 0; i < list.size(); i++) {
                int be = i * (font.length() + font2.length());// 被标签占用的位置
                content = content.substring(0, be + list.get(i).start)
                        + font
                        + content.substring(be + list.get(i).start,
                        be + list.get(i).end + 2)
                        + font2
                        + content.substring(be + list.get(i).end + 2,
                        content.length());
            }
        } catch (Exception e) {
            System.out.println("高亮处理错误！");
            e.printStackTrace();
            if(isCut)return cutContent+"...";
        }
        return isCut?content+"...":content;
    }

    /**
     * 获取关键词匹配下标
     *
     * @author：Ivan
     * @date：2015年11月3日 上午10:53:31
     * @param content
     * @param str
     * @param isHomophony
     * @return
     */
    public static List<Postion> getAllIndex (String content, String str,
                                             boolean isHomophony) throws Exception{
        String cs[] = content.split("");
        String strs[] = { "", str.toUpperCase() };
        if (isChinese(str)) {
            strs = str.split("");
        }

        // 所有匹配的下标位置集合
        List<Postion> list = new ArrayList<Postion>();
        Postion point = null; // 用来存储匹配的开始下标、结束下标
        if (isHomophony) {// 如果同音，将每一项转换成拼音
            // 将内容转换成拼音
            for (int i = 0; i < cs.length; i++) {
                if (StringUtils.isNotBlank(cs[i].trim())) {
                    cs[i] = PinyinTool.toPinYin(cs[i]);
                }
            }
            // 将关键词转成拼音
            for (int i = 1; i < strs.length; i++) {
                if (StringUtils.isNotBlank(strs[i].trim())) {
                    strs[i] = PinyinTool.toPinYin(strs[i]);
                }
            }

            if (isChinese(str)) {// 纯汉字匹配
                for (int i = 1; i < cs.length; i++) {

                    int count = 0;// 成果匹配次数
                    if (i + strs.length - 1 > cs.length)
                        break;// 剩余匹配个数不足时跳出
                    for (int j = 1; j < strs.length; j++) {
                        if (cs[i + j - 1].equals(strs[j]))
                            count++;
                    }
                    // 当关键词的每一项都匹配时添加匹配下标
                    if (count == strs.length - 1) {
                        point = new Postion(i - 1, i - 1 + strs.length - 2);
                        list.add(point);
                    }
                }
                //获取拼音关键词在文本中所有下标位置
                String index = getAllIndex(content.toUpperCase(), PinyinTool.toPinYin(str), 0);
                if(StringUtils.isNotBlank(index)){
                    // System.out.println(index);
                    String indexs[] = index.split(",");
                    for (int i = 0; i < indexs.length; i++) {
                        int n = Integer.parseInt(indexs[i]);//开始下标位置
                        point = new Postion(n, n+PinyinTool.toPinYin(str).length() - 1);
                        list.add(point);
                    }
                }
            } else {// 混合匹配
                //获取拼音关键词在文本中所有下标位置
                String index = getAllIndex(PinyinTool.toPinYin(content.toUpperCase()), strs[1], 0);
                if(StringUtils.isNotBlank(index)){
                    // System.out.println(index);
                    String indexs[] = index.split(",");

                    //处理逻辑：
                    //  如果拼音关键词开始位置是文本内容中某个拼音的开始位置，并且拼音关键词的结束位置是文本内容中某个拼音的结束位置，
                    //视为有效关键词，并记录该拼音关键词的开始下标和结束下标。
                    int count = 0;//成功匹配次数，两次为有效匹配，否则为无效
                    for (int i = 0; i < indexs.length; i++) {
                        int n = Integer.parseInt(indexs[i]);//开始下标位置
                        int sum = 0;//下标累计
                        int start = 0;//开始下标
                        int end = 0;//结束下标
                        for (int j = 0; j < cs.length; j++) {
                            if (n == sum && count == 0) {//满足开始位置，成功匹配一次
                                count = 1;
                                start = j;
                            }
                            sum += cs[j].length();//累加文本下标位置
                            if (n + strs[1].length() == sum && count == 1) {//满足结束位置，成功匹配两次
                                count = 2;
                                end = j;
                                break;
                            }
                        }
                        if (count == 2) {//成功匹配两次，保存有效起始下标位置
                            point = new Postion(start, end - 1);
                            list.add(point);
                        }
                        count = 0;//初始化匹配次数
                    }
                }
            }
        }else{
            //获取拼音关键词在文本中所有下标位置
            String index = getAllIndex(content.toUpperCase(), str, 0);
            if(StringUtils.isNotBlank(index)){
                // System.out.println(index);
                String indexs[] = index.split(",");
                for (int i = 0; i < indexs.length; i++) {
                    int n = Integer.parseInt(indexs[i]);//开始下标位置
                    point = new Postion(n, n+str.length() - 1);
                    list.add(point);
                }
            }
        }
        // System.out.println("关键词：" + strs[1]);

        return list;
    }

    /**
     * 获取字符串匹配下标
     *
     * @author：Ivan
     * @date：2015年11月3日 上午10:53:13
     * @param content
     * @param str
     * @param index
     * @return
     */
    public static String getAllIndex(String content, String str, int index) {
        int n = content.indexOf(str, index);
        if (n != -1) {
            // 递归匹配所有下标位置
            return n + "," + getAllIndex(content, str, n + 1);
        } else {
            return "";
        }
    }

    /**
     * 判断关键词是否为汉字
     *
     * @author：Ivan
     * @date：2015年11月3日 下午3:05:55
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }

    /**
     * 判断关键词是否包含汉字
     *
     * @author：Ivan
     * @date：2015年11月3日 下午3:05:55
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
