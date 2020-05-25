package com.cbb.seeandroid.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by 坎坎.
 * Date: 2020/5/24
 * Time: 20:01
 * describe:
 */
@Aspect
public class MyAopTest {
    /*
    * 切入点
    * @com.cbb.seeandroid.aop.LogTrace 切入的注解类全路径
    *                          * *(..)) 所有类  所有方法（不限参数）
    * */
    @Pointcut("execution(@com.cbb.seeandroid.aop.LogTrace * *(..))")
    public void pointCut(){
    }
    @Around("pointCut()")
    public Object logs(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        // 切入点全类名
        String declaringTypeName = signature.getDeclaringTypeName();
        // 切入点方法名
        String name = signature.getName();
        Log.e("----------->",declaringTypeName+"中的"+name+"方法");
        Log.e("----------->","方法调用前");
        Object proceed = joinPoint.proceed();
        Log.e("----------->","方法调用后");
        return proceed;
    }
}
