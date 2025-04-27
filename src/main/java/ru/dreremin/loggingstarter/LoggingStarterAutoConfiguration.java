package ru.dreremin.loggingstarter;

import feign.Logger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import ru.dreremin.loggingstarter.aspect.LogExecutionAspect;
import ru.dreremin.loggingstarter.feign.FeignLogger;
import ru.dreremin.loggingstarter.masking.JsonBodyPropertiesMasker;
import ru.dreremin.loggingstarter.property.LoggingStarterProperties;
import ru.dreremin.loggingstarter.service.LoggingService;
import ru.dreremin.loggingstarter.webfilter.WebLoggingFilter;
import ru.dreremin.loggingstarter.webfilter.WebLoggingRequestBodyAdvice;

@AutoConfiguration
@ConditionalOnProperty(prefix = "logging", value = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingStarterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "logging", value = "log-exec-time", havingValue = "true")
    public LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "enabled", havingValue = "true", matchIfMissing = true)
    public WebLoggingFilter loggingFilter() {
        return new WebLoggingFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = {"enabled"}, havingValue = "true")
    public WebLoggingRequestBodyAdvice loggingRequestBodyAdvice() {
        return new WebLoggingRequestBodyAdvice();
    }

    @Bean
    public LoggingStarterProperties loggingStarterProperties() {
        return new LoggingStarterProperties();
    }

    @Bean
    public JsonBodyPropertiesMasker jsonBodyPropertiesMasker() {
        return new JsonBodyPropertiesMasker();
    }

    @Bean
    public LoggingService loggingService() {
        return new LoggingService();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "log-feign-requests", havingValue = "true")
    public FeignLogger feignLogger() {
        return new FeignLogger();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "log-feign-requests", havingValue = "true")
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
