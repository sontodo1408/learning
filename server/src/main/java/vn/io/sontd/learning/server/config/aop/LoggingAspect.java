package vn.io.sontd.learning.server.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * Cross-cutting logging for every controller endpoint.
 * Wraps any method annotated with (or declared inside a class annotated with)
 * {@link RequestMapping}, logging the incoming request arguments, the outgoing
 * response, and any exception thrown.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Logs the request, response, and error (if any) around a controller method call.
     *
     * @param joinPoint      the intercepted method invocation
     * @param requestMapping the matched {@link RequestMapping} annotation (unused directly,
     *                       required only to scope the pointcut)
     * @return the original return value of the intercepted method
     * @throws Throwable rethrows whatever exception the intercepted method threw
     */
    @Around("@within(requestMapping) || @annotation(requestMapping)")
    public Object logAround(ProceedingJoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("➡️ [REQUEST ] [{}] | request = {}", methodName, Arrays.toString(args));

        try {
            // Proceed with the actual controller method execution
            Object result = joinPoint.proceed();
            log.info("✅ [RESPONSE] [{}] | response = {}", methodName, result);
            return result;
        } catch (Exception ex) {
            // Log the cause separately when present for easier root-cause analysis
            if (ex.getCause() != null) {
                log.error("❌ [ERROR] [{}] | message = {} | cause = {}", methodName, ex.getMessage(), ex.getCause().getMessage(), ex);
            } else {
                log.error("❌ [ERROR] [{}] | message = {}", methodName, ex.getMessage(), ex);
            }
            throw ex;
        }
    }
}
