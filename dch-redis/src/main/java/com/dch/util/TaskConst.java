package com.dch.util;

/**
 * Created by sunkqa on 2018/3/8.
 */
public class TaskConst {
    public static final taskCycle TASK_CYCLE = new taskCycle();
    public static class taskCycle extends GenericEnum{
//        public static final long FIFTEEN_MINUTE = 1;
//        public static final long ONE_HOUR = 2;
//        public static final long TWO_HOUR = 3;
//        public static final long FOUR_HOUR = 4;
//        public static final long EIGHT_HOUR = 5;
        public static final long TWELVE_HOUR = 1;
        public static final long TWENTY_FOUR_HOUR = 2;
        public static final long FOURTY_EIGHT_HOUR = 3;
        public static final long A_WEEK = 4;

        private  taskCycle() {
//            super.putEnum(Long.valueOf(1L), "15分钟");
//            super.putEnum(Long.valueOf(2L), "1小时");
//            super.putEnum(Long.valueOf(3L), "2小时");
//            super.putEnum(Long.valueOf(4L), "4小时");
//            super.putEnum(Long.valueOf(5L), "8小时");
            super.putEnum(Long.valueOf(1L), "12小时");
            super.putEnum(Long.valueOf(2L), "24小时");
            super.putEnum(Long.valueOf(3L), "48小时");
            super.putEnum(Long.valueOf(4L), "一周");
        }
    }
}
