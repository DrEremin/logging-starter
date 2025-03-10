package ru.dreremin.loggingstarter.exclusion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.PathMatcher;

import java.util.List;

public class EndpointExclusionFlagQualifier {

    @Autowired
    private PathMatcher pathMatcher;

    private boolean exclusion;

    public void setExclusionFlag(String endpointURI, List<String> uriPaths) {
        exclusion = false;

        for (String path : uriPaths) {
            if (pathMatcher.match(path, endpointURI)) {
                exclusion = true;
                return;
            }
        }
    }

    public boolean isNotExclusion() {
        return !exclusion;
    }
}
