package ru.dreremin.loggingstarter.webfilter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.service.LoggingService;
import ru.dreremin.loggingstarter.util.URIPathMatcherWithPathPatterns;

import java.lang.reflect.Type;

@ControllerAdvice
public class WebLoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoggingStarterProperties loggingStarterProperties;

    @Autowired
    private LoggingService loggingService;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return !URIPathMatcherWithPathPatterns.matchAny(request.getRequestURI(), loggingStarterProperties.getUriPaths());
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        loggingService.logRequestBody(request, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}