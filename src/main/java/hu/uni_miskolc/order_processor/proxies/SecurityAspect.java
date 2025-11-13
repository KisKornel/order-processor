package hu.uni_miskolc.order_processor.proxies;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
@Slf4j
public class SecurityAspect {
    @Pointcut("execution(* hu.uni_miskolc.order_processor.services.*.*(..))")
    private void serviceMethodsPointcut() {}

    @Before("serviceMethodsPointcut()")
    public void checkSecurityBefore(JoinPoint joinPoint) {
        log.info("SecurityAspect Before method perform. Method invoked: {}", joinPoint.getSignature().getName());
    }
}
