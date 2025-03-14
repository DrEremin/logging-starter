package ru.dreremin.loggingstarter.masking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public class JsonBodyPropertiesMasker {

    private static final Logger log = LoggerFactory.getLogger(JsonBodyPropertiesMasker.class);

    @Autowired
    private ObjectMapper objectMapper;

    public Optional<String> maskProperties(Object body, List<String> bodyPaths) {
        Optional<String> newBodyOptional = Optional.empty();

        if (body == null) {
            return newBodyOptional;
        }

        String bodyString = body.getClass() != String.class ? convertObjectToJsonString(body) : (String) body;

        if (StringUtils.hasText(bodyString)) {
            return newBodyOptional;
        }

        try {
            DocumentContext documentContext = JsonPath.parse(bodyString);

            for (String path : bodyPaths) {
                try {
                    documentContext = documentContext.map(path, (val, conf) -> "***");
                } catch (PathNotFoundException ignore) {
                }
            }

            newBodyOptional = Optional.ofNullable(documentContext.jsonString())
                    .filter(StringUtils::hasText);
        } catch (UnsupportedOperationException e) {
            log.info("Строка тела не соответствует формату JSON");
        }

        return newBodyOptional;
    }

    private String convertObjectToJsonString(Object obj) {
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать объект тела в строку JSON");
        }

        return jsonString;
    }
}
