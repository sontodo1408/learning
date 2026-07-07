package vn.io.sontd.learning.server.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@within(requestMapping) || @annotation(requestMapping)")
    public Object logAround(ProceedingJoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ [REQUEST ] [{}] | request = {}", methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            log.info("✅ [RESPONSE] [{}] | response = {}", methodName, result);
            return result;
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                log.error("❌ [ERROR] [{}] | message = {} | cause = {}", methodName, ex.getMessage(), ex.getCause().getMessage(), ex);
            } else {
                log.error("❌ [ERROR] [{}] | message = {}", methodName, ex.getMessage(), ex);
            }
            throw ex;
        }
    }
}
