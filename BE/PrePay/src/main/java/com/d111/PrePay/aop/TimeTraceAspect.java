package com.d111.PrePay.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeTraceAspect {

    @Pointcut("execution(* com.d111.PrePay.service.*.*(..))")
    public void monitorPerformance(){

    }

    @Around("monitorPerformance()")
    public Object measureMethodTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("메서드 : {}, 실행 시간 : {}ms",joinPoint.getSignature(),executionTime);
        return result;

    }

}
