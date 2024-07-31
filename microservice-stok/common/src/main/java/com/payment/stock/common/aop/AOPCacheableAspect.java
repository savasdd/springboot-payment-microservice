package com.payment.stock.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.stock.common.base.BaseResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class AOPCacheableAspect {
    private final ObjectMapper mapper;


    public AOPCacheableAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("@annotation(com.payment.stock.common.aop.AOPCacheable) && execution(* *(..))")
    public void aopAnnotation() {
    }


    @Around(value = "aopAnnotation() && @annotation(aop)")
    public BaseResponse aopCacheable(ProceedingJoinPoint joinPoint, AOPCacheable aop) throws Throwable {
        Object result = null;

        Class<? extends Object> sinif = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method metot = signature.getMethod();
        AOPCacheable annotation = metot.getAnnotation(AOPCacheable.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ///// Before Method Execution /////
        result = joinPoint.proceed();
        ///// After Method Execution /////

        BaseResponse response = (BaseResponse) result;
        if (ObjectUtils.isNotEmpty(response)) {
            var resp = List.of(convertObjectToJson(response));
            System.out.println(resp);
        }

        return response;
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        return mapper.writeValueAsString(object);
    }

    public BaseResponse findAll(BaseResponse baseResponse) {
        return baseResponse;
    }
}