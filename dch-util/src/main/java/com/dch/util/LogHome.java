package com.dch.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2017/9/14.
 */
public class LogHome {

    public LogHome()
    {
    }

    public static Log getLog()
    {
        return LogFactory.getLog(com.dch.util.LogHome.class);
    }

    public static Log getLog(Class cls)
    {
        return LogFactory.getLog(cls);
    }

    public static Log getLog(String name)
    {
        return LogFactory.getLog(name);
    }
}
