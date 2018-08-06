package com.pokepet.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.pokepet.annotation.ResponseResult;
import com.pokepet.interceptor.ResponseResultInterceptor;
import com.pokepet.result.DefaultErrorResult;
import com.pokepet.result.PlatformResult;
import com.pokepet.result.Result;
import com.pokepet.util.RequestContextHolderUtil;

/**
 * @desc 接口响应体处理器
 * 
 * @author zhumaer
 * @since 4/1/2018 3:00 PM
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ResponseResult responseResultAnn = (ResponseResult) RequestContextHolderUtil.getRequest().getAttribute(ResponseResultInterceptor.RESPONSE_RESULT);
        return responseResultAnn == null ? false : true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseResult responseResultAnn = (ResponseResult) RequestContextHolderUtil.getRequest().getAttribute(ResponseResultInterceptor.RESPONSE_RESULT);

        Class<? extends Result> resultClazz = responseResultAnn.value();

        if (resultClazz.isAssignableFrom(PlatformResult.class)) {
            if (body instanceof DefaultErrorResult) {
                DefaultErrorResult defaultErrorResult = (DefaultErrorResult) body;
                PlatformResult a = new PlatformResult();
                a.setCode(Integer.valueOf(defaultErrorResult.getCode()));
                a.setMsg(defaultErrorResult.getMessage());
                a.setData(defaultErrorResult.getErrors());
//                return PlatformResult.builder()
//                        .code(Integer.valueOf(defaultErrorResult.getCode()))
//                        .msg(defaultErrorResult.getMessage())
//                        .data(defaultErrorResult.getErrors())
//                        .build();
                return a;
            }

            return PlatformResult.success(body);
        }

        return body;
    }

}