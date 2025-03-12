package ru.dreremin.loggingstarter.webfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import ru.dreremin.loggingstarter.util.URIPathMatcherWithPathPatterns;
import ru.dreremin.loggingstarter.util.JsonBodyPropertiesMasker;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.util.RequestDataFormatter;

import java.lang.reflect.Type;

@ControllerAdvice
public class WebLoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingRequestBodyAdvice.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoggingStarterProperties loggingStarterProperties;

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
        String method = request.getMethod();
        String requestURI = RequestDataFormatter.formatRequestUriWithQueryParams(request);
        String headers = RequestDataFormatter.formatRequestHeaders(request, loggingStarterProperties.getHeaders());
        log.info("Обработка тела запроса...");
        String jsonBody = JsonBodyPropertiesMasker.maskProperties(body, loggingStarterProperties.getBodyPaths());
        log.info("Запрос: {} {} {} {}", method, requestURI, headers, "body=" + jsonBody);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}