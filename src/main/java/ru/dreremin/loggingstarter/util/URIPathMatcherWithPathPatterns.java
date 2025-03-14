package ru.dreremin.loggingstarter.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;

public class URIPathMatcherWithPathPatterns {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static boolean matchAny(String uriPath, List<String> pathPatterns) {
        for (String pathPattern : pathPatterns) {
            if (pathMatcher.match(pathPattern, uriPath)) {
                return true;
            }
        }

        return false;
    }
}
