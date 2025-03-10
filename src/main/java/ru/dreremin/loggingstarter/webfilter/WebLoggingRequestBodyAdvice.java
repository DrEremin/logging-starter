package ru.dreremin.loggingstarter.webfilter;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.dreremin.loggingstarter.exclusion.EndpointExclusionFlagQualifier;
import ru.dreremin.loggingstarter.exclusion.PropertiesCutterFromBody;
import ru.dreremin.loggingstarter.property.AppProperties;
import ru.dreremin.loggingstarter.util.RequestDataFormatter;

import java.lang.reflect.Type;

@ControllerAdvice
public class WebLoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingRequestBodyAdvice.class);

    @Autowired
    private EndpointExclusionFlagQualifier endpointExclusionFlagQualifier;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppProperties appProperties;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return endpointExclusionFlagQualifier.isNotExclusion();
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        String method = request.getMethod();
        String requestURI = RequestDataFormatter.formatRequestUriWithQueryParams(request);
        String headers = RequestDataFormatter.formatRequestHeaders(request, appProperties.getHeaders());

        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            jsonBody = "body=" + PropertiesCutterFromBody.cutProperties(jsonBody, appProperties.getBodyPaths());
            log.info("Запрос: {} {} {} {}", method, requestURI, headers, jsonBody);
        } catch (JsonProcessingException ignored) {
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}