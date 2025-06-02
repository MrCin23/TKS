package pl.lodz.p.user.rest.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Aspect @Order(Ordered.LOWEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class CountedAspect {

    private final MeterRegistry registry;
    private final ConcurrentHashMap<String, Counter> counterCache = new ConcurrentHashMap<>();

    @Around("Pointcuts.countedMethodOrClass()")
    public Object countMethod(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        String metricName = method.getName() + "_calls_total";

        Counter counter = counterCache.computeIfAbsent(metricName, name ->
                Counter.builder(name)
                        .description("Auto-counted method call for: " + method)
                        .register(registry)
        );

        counter.increment();
        return pjp.proceed();
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }
}