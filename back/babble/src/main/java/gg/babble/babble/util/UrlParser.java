package gg.babble.babble.util;

import gg.babble.babble.exception.BabbleIllegalStatementException;
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
        urlParsingValidate(url);
        Map<String, String> variables = PATH_MATCHER.extractUriTemplateVariables(ROOM_SUBSCRIBE_URL_PATTERN.urlPattern, url);
        return Long.valueOf(variables.get("roomId"));
    }

    private static void urlParsingValidate(final String url) {
        if (!PATH_MATCHER.match(ROOM_SUBSCRIBE_URL_PATTERN.urlPattern, url)) {
            throw new BabbleIllegalStatementException("roomId를 파싱할 수 없는 url 입니다.");
        }
    }
}
