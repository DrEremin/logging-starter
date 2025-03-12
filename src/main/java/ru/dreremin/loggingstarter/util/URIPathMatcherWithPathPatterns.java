package ru.dreremin.loggingstarter.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;

public class URIPathMatcherWithPathPatterns {

    @Autowired
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static boolean matchAny(String uriPath, List<String> pathPatterns) {
        boolean isMatch = false;

        for (String pathPattern : pathPatterns) {
            if (pathMatcher.match(pathPattern, uriPath)) {
                isMatch = true;
                break;
            }
        }

        return isMatch;
    }
}
