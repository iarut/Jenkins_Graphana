package org.example.config;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* org.example.controller.*.*(..))")
    public void logBeforeEachRequest() {
        logger.info("A method in controller is about to be called...");
    }

    @After("execution(* org.example.controller..*.*(..))")
    public void logAfterEachRequestFinally() {
        logger.info("A method in controller has finished execution.");
    }

    @AfterReturning("execution(* org.example.controller.*.*(..))")
    public void logAfterEachRequest() {
        logger.info("A method in controller has been completed successfully.");
    }

    @AfterThrowing("execution(* org.example.controller.*.*(..))")
    public void logAfterThrowing() {
        logger.error("An exception occurred in a controller method.");
    }
}
