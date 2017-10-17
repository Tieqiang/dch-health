package org.wltea.analyzer.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/24.
 */
public class Postion {
    public int start = 0;// 开始位置
    public int end = 0;// 结束位置
    private boolean isUnion=false;//是否被合并

    public Postion() {

    }

    public Postion(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 位置合并
     *
     * @author：Ivan
     * @date：2015年11月3日 下午12:51:04
     * @param postion
     * @return
     */
    @SuppressWarnings("serial")
    public boolean union(Postion postion) {
        boolean b = false;
        if (postion != null) {
            Set<Integer> result = new HashSet<Integer>();
            Set<Integer> set1 = new HashSet<Integer>() {
                {
                    for (int i = start; i <= end; i++) {
                        add(i);
                    }
                }
            };

            final int s = postion.start, e = postion.end;
            Set<Integer> set2 = new HashSet<Integer>() {
                {
                    for (int i = s; i <= e; i++) {
                        add(i);
                    }
                }
            };

            //1.取交集
            result.clear();
            result.addAll(set1);
            result.retainAll(set2);
            if (result.size() > 0 ||this.end==postion.start-1 || this.start+1==postion.end) {//判断是否存在交集，存在交集的情况下执行并集操作
                //2.取并集
                result.clear();
                result.addAll(set1);
                result.addAll(set2);
                for (Integer integer : set2) {
                    if (integer < start)
                        start = integer;
                    if (integer > end)
                        end = integer;
                }
                b = true;
                postion.setUnion(b);//标记被合并
            }
        }
        return b;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isUnion() {
        return isUnion;
    }

    public void setUnion(boolean isUnion) {
        this.isUnion = isUnion;
    }

    @Override
    public String toString() {
        return start+","+end+":"+isUnion;
    }
}
