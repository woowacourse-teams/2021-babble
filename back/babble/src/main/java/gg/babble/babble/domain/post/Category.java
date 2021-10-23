package gg.babble.babble.domain.post;

import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;

public enum Category {
    FREE("자유"),
    SUGGESTIONS("건의"),
    GAME("게임"),
    NOTICE("공지");

    private final String name;

    Category(final String name) {
        this.name = name;
    }

    public static Category from(final String name) {
        return Arrays.stream(Category.values())
            .filter(category -> category.name.equals(name))
            .findAny()
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s] 이름의 카테고리는 존재하지 않습니다.", name)));
    }

    public String getName() {
        return name;
    }

    public boolean isNotice() {
        return this == NOTICE;
    }
}
