package com.stackroute.keepnote.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */
@Aspect
@Component
public class LoggingAspect {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/*
	 * Write loggers for each of the methods of Category controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	@Before("execution(* com.stackroute.keepnote.controller.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Entering into class" + joinPoint.getClass());
	}
	
	@After("execution(* com.stackroute.keepnote.controller.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.info("Exiting from class" + joinPoint.getClass());
	}
}
