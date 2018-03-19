package com.dch.util;

/**
 * Created by sunkqa on 2018/3/8.
 */
public class TaskConst {
    public static final taskUnite TASK_CYCLE = new taskUnite();
    public static class taskUnite extends GenericEnum{
        public static final long MINUTE = 1;
        public static final long HOUR = 2;
        public static final long DAY = 3;
        public static final long WEEK = 4;

        private  taskUnite() {
            super.putEnum(Long.valueOf(1L), "minute");
            super.putEnum(Long.valueOf(2L), "hour");
            super.putEnum(Long.valueOf(3L), "day");
            super.putEnum(Long.valueOf(4L), "week");
        }
    }
}
