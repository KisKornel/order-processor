package hu.uni_miskolc.order_processor.proxies;

import hu.uni_miskolc.order_processor.apis.HasUserId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Objects;

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
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(a -> a instanceof HasUserId)
                .findFirst()
                .map(a -> (HasUserId) a)
                .ifPresentOrElse(
                        body -> {
                            String bodyUserId = body.getUserId();
                            if (bodyUserId == null) {
                                try {
                                    throw new AccessDeniedException("Missing userId in request body");
                                } catch (AccessDeniedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            String currentUserId = "user-123";
                            if (!bodyUserId.equals(currentUserId)) {
                                try {
                                    throw new AccessDeniedException("User ID does not match authenticated user");
                                } catch (AccessDeniedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        () -> {
                            try {
                                throw new AccessDeniedException("Request body does not contain userId");
                            } catch (AccessDeniedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
