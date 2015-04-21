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
        LOGGER.info("Result for searching [{}] is [{}]", joinPoint.getArgs()[0], returnValue);
    }

    @AfterReturning(pointcut = "execution(* com.techlooper.service.JobStatisticService.count*())",
            returning = "returnValue")
    public void logServiceCountResult(final JoinPoint joinPoint, final Object returnValue) {
        LOGGER.info("Result for searching by [{}] is [{}]", joinPoint.getSignature().getName(), returnValue);
    }

    /* location / {
    proxy_pass  ;
    proxy_redirect off;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;
    proxy_read_timeout 300s;
    proxy_buffering off;
    rewrite ^/(.*) /#/bubble-chart/ break;
  }
}*/
}
