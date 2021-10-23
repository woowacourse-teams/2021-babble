package gg.babble.babble.domain.post;

import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashMap;
import java.util.Map;

public enum Category {
    FREE("자유"),
    SUGGESTIONS("건의"),
    GAME("게임"),
    NOTICE("공지");

    private static final Map<String, Category> CATEGORY_MAP = new HashMap<>();

    static {
        for (Category category : values()) {
            CATEGORY_MAP.put(category.name, category);
        }
    }

    private final String name;

    Category(final String name) {
        this.name = name;
    }

    public static Category getCategoryByName(final String name) {
        if (!CATEGORY_MAP.containsKey(name)) {
            throw new BabbleNotFoundException(String.format("[%s] 이름의 카테고리는 존재하지 않습니다.", name));
        }
        return CATEGORY_MAP.get(name);
    }

    public String getName() {
        return name;
    }

    public boolean isNotice() {
        return this == NOTICE;
    }
}
