package gg.babble.babble.util;

import java.util.Map;
import org.springframework.util.AntPathMatcher;

public enum UrlParser {
    ROOM_SUBSCRIBE_URL_PATTERN("/topic/rooms/{roomId}/*");

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final String urlPattern;

    UrlParser(final String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public static Long getRoomId(final String url) {
        Map<String, String> variables = PATH_MATCHER.extractUriTemplateVariables(ROOM_SUBSCRIBE_URL_PATTERN.urlPattern, url);
        return Long.valueOf(variables.get("roomId"));
    }
}
