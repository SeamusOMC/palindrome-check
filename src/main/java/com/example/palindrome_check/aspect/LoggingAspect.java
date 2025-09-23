package com.example.palindrome_check.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    @AfterReturning(pointcut = "execution(* com.example.palindrome_check.service..*(..))",
            returning = "result")
    public void logServiceCalls(JoinPoint joinPoint, Object result) {
        log.info("Service method {} called with args {} → returned {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs(),
                result);
    }
}
