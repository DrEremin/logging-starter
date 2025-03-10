package ru.dreremin.loggingstarter.util;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.List;

public class PropertiesCutterFromBody {

    public static String cutProperties(String body, List<String> bodyPaths) {
        String newBody = "{}";

        try {
            DocumentContext documentContext = JsonPath.parse(body);

            for (String path : bodyPaths) {
                try {
                    documentContext = documentContext.delete(path);
                } catch (PathNotFoundException ignore) {
                }
            }

            newBody = documentContext.jsonString();
        } catch (UnsupportedOperationException | IllegalArgumentException ignore) {
        }

        return newBody;
    }
}
