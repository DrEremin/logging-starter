package ru.dreremin.loggingstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import ru.dreremin.loggingstarter.aspect.LogExecutionAspect;
import ru.dreremin.loggingstarter.property.AppProperties;
import ru.dreremin.loggingstarter.exclusion.EndpointExclusionFlagQualifier;
import ru.dreremin.loggingstarter.webfilter.WebLoggingFilter;
import ru.dreremin.loggingstarter.webfilter.WebLoggingRequestBodyAdvice;

@AutoConfiguration
@ConditionalOnProperty(prefix = "logging", value = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingStarterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "logging", value = "log-exec-time", havingValue = "true")
    LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "enabled", havingValue = "true", matchIfMissing = true)
    WebLoggingFilter loggingFilter() {
        return new WebLoggingFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = {"enabled", "log-body"}, havingValue = "true")
    WebLoggingRequestBodyAdvice loggingRequestBodyAdvice() {
        return new WebLoggingRequestBodyAdvice();
    }

    @Bean
    AppProperties appProperties() {
        return new AppProperties();
    }

    @Bean
    PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    EndpointExclusionFlagQualifier endpointExclusionFlagQualifier() {
        return new EndpointExclusionFlagQualifier();
    }
}
