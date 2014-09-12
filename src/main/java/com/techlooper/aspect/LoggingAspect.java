package com.techlooper.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by chrisshayan on 7/15/14.
 */
@Service
@Aspect
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning(pointcut = "execution(* com.techlooper.service.JobStatisticService.count(..))",
                    returning = "returnValue")
    public void logServiceResult(final JoinPoint joinPoint, final Object returnValue) {
        LOGGER.info(String.format("Result for searching [%s] is [%s]", joinPoint.getArgs()[0], returnValue));
    }
    
    @AfterReturning(pointcut = "execution(* com.techlooper.service.JobStatisticService.count*())",
          returning = "returnValue")
    public void logServiceCountResult(final JoinPoint joinPoint, final Object returnValue) {
       LOGGER.info(String.format("Result for searching by [%s] is [%s]", joinPoint.getSignature().getName(), returnValue));
    }
}
