package support;


import annotation.Cache;
import annotation.CacheInsert;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class CacheImpl {

    private final CacheClient cacheClient;

    public CacheImpl(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    @Pointcut("@annotation(annotation.Cache)")
    public void cacheMethod() {
    }

    @Pointcut("@annotation(annotation.CacheInsert)")
    public void cachePutMethod() {
    }

    @Pointcut("@annotation(annotation.CacheDelete)")
    public void cacheDeleteMethod() {
    }

    @Around("cacheMethod()")
    public Object aroundCache(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint == null) {
            return null;
        }
        Object value = cacheClient.getValue(getKey(joinPoint));
        if (value != null) {
            return value;
        }
        Object proceed = joinPoint.proceed();
        /*从切面值入点获取植入点方法*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        /*获取切入点方法*/
        Method method = signature.getMethod();
        /*获取方法上的值*/
        Cache cache = method.getAnnotation(Cache.class);
        cacheClient.putValue(getKey(joinPoint), proceed, cache.expire());
        return proceed;
    }

    @Around("cachePutMethod()")
    public Object aroundCachePut(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint == null) {
            return null;
        }
        Object value = cacheClient.getValue(getKey(joinPoint));
        if (value != null) {
            cacheClient.remove(getKey(joinPoint));
        }
        Object proceed = joinPoint.proceed();
        /*从切面值入点获取植入点方法*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        /*获取切入点方法*/
        Method method = signature.getMethod();
        /*获取方法上的值*/
        CacheInsert cacheInsert = method.getAnnotation(CacheInsert.class);
        if (cacheInsert != null) {
            cacheClient.putValue(getKey(joinPoint), proceed, cacheInsert.expire());
        }
        return proceed;
    }

    @Around("cacheDeleteMethod()")
    public Object aroundCacheDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint == null) {
            return null;
        }
        Object proceed = joinPoint.proceed();
        Object value = cacheClient.getValue(getKey(joinPoint));
        if (value != null) {
            cacheClient.remove(getKey(joinPoint));
        }
        return proceed;
    }

    /**
     * 根据参数生成cachekey
     *
     * @param joinPoint
     * @return
     */
    private String getKey(ProceedingJoinPoint joinPoint) {
        String clazzName = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName();
        String result = clazzName + "_" + method;
        if (joinPoint.getArgs() == null) {
            return result;
        }
        for (Object key : joinPoint.getArgs()) {
            if (key != null) {
                result += "_" + key.toString();
            }
        }
        return result;
    }

}
