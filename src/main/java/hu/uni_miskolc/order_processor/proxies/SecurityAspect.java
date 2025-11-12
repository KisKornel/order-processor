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
@Order(2)
public class SecurityAspect {
    @Pointcut("execution(* hu.uni_miskolc.order_processor.services.*.*(..))")
    public void serviceMethodsPointcut() {}

    @Before("serviceMethodsPointcut()")
    public void checkSecurityBefore(JoinPoint joinPoint) {
        System.out.println("SecurityAspect Before method perform. Method invoked: " + joinPoint.getSignature().getName());
    }

    @After("serviceMethodsPointcut()")
    public void checkSecurityAfter(JoinPoint joinPoint) {
        System.out.println("SecurityAspect After method perform. Method invoked: " + joinPoint.getSignature().getName());
    }
}
