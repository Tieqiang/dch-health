package com.dch.aop;

import com.dch.util.StringUtils;
import com.dch.vo.PageParam;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by sunkqa on 2019/1/22.
 */
@Aspect
@Component
public class HotKeywordsAop {

    public final static String HOT_WORDS = "hot_words";

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut(value = "execution(* com.dch.service.FrontCategorySearchService.*ByKeyWords(..))")
    private void pointcut(){}

    @Before(value = "pointcut()")
    public void beforeMethod(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(args!=null && args.length>0){
            PageParam pageParam = (PageParam)args[0];
            String keyword = pageParam.getKeyWords();
            keyword = StringUtils.remeveHtmlLabel(keyword);
            if(!StringUtils.isEmptyParam(keyword) && !"*".equals(keyword)){
                redisTemplate.opsForZSet().incrementScore(HOT_WORDS,keyword,1d);
            }
        }
    }
}
