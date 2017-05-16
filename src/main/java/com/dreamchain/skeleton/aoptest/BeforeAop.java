package com.dreamchain.skeleton.aoptest;

import com.dreamchain.skeleton.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by LAPTOP DREAM on 8/14/2016.
 */
public class BeforeAop implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {

        Log lg=LogFactory.getLog(User.class);
        lg.info(o.toString());
        System.out.println(o.toString());


    }
}
