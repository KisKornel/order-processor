package hu.uni_miskolc.order_processor.proxies;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LoggingAspect {
    @Pointcut("execution(* hu.uni_miskolc.order_processor.services.*.*(..))")
    public void serviceMethodsPointcut() {}

    @Before("serviceMethodsPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("LoggingAspect Before method perform. Method invoked: " + joinPoint.getSignature().getName());
    }

    @After("serviceMethodsPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("LoggingAspect After method perform. Method invoked: " + joinPoint.getSignature().getName());
    }
}
