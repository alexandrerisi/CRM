package com.risi.mvc.data.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CRMLoggingAspect {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.risi.mvc.data.demo.controller.*.*(..))")
    private void forControllerPackage() {}

    @Pointcut("execution(* com.risi.mvc.data.demo.service.*.*(..))")
    private void forServicePackage() {}

    @Pointcut("execution(* com.risi.mvc.data.demo.repository.*.*(..))")
    private void forRepositoryPackage() {}

    // This is a combination that will execute all Point cuts above
    @Pointcut("forControllerPackage() || forServicePackage() || forRepositoryPackage()")
    private void forAppFlow() {}

    @Before("forAppFlow()")
    public void before(JoinPoint joinPoint) {
        logger.info("===> in @Before: calling method: " + joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args)
            logger.info("===> argument: " + arg);
    }

    @AfterReturning(pointcut = "forAppFlow()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        logger.info("===> in @AfterReturning: from method: " + joinPoint.getSignature().toShortString());
        logger.info("===> result: " + returnValue);
    }
}
