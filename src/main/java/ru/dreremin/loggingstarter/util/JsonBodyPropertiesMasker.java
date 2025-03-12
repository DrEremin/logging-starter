package ru.dreremin.loggingstarter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonBodyPropertiesMasker {

    private static final Logger log = LoggerFactory.getLogger(JsonBodyPropertiesMasker.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static String maskProperties(Object body, List<String> bodyPaths) {
        String bodyString = body.getClass() != String.class ? convertObjectToJsonString(body) : (String) body;
        String newBody = "{}";

        try {
            DocumentContext documentContext = JsonPath.parse(bodyString);

            for (String path : bodyPaths) {
                try {
                    documentContext = documentContext.set(path, "***");
                } catch (PathNotFoundException ignore) {
                    log.info("Свойство тела по шаблону '{}' не найдено", path);
                }
            }

            newBody = documentContext.jsonString();
        } catch (UnsupportedOperationException e) {
            log.info("Строка тела не соответствует формату JSON");
        } catch (IllegalArgumentException e) {
            log.info("Строка тела ранна null");
        }

        return newBody;
    }

    private static String convertObjectToJsonString(Object obj) {
        String jsonString = "{}";

        try {
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info("Не удалось преобразовать объект тела в строку JSON");
        }
        return jsonString;
    }
}
