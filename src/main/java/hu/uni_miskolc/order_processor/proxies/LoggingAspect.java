package hu.uni_miskolc.order_processor.proxies;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* hu.uni_miskolc.order_processor.services.*.*(..))")
    private void serviceMethodsPointcut() {}

    @Before("serviceMethodsPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("LoggingAspect Before method perform. Method invoked: {}", joinPoint.getSignature().getName());
    }

    @After("serviceMethodsPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("LoggingAspect After method perform. Method invoked: {}", joinPoint.getSignature().getName());
    }
}
