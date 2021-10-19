package gg.babble.babble.domain.board;

import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;

public enum BoardSearchType {
    TITLE("제목"),
    TITLE_AND_CONTENT("제목 + 내용"),
    AUTHOR("작성자"),
    CATEGORY("카테고리");

    private final String name;

    BoardSearchType(final String name) {
        this.name = name;
    }

    public static BoardSearchType of(final String name) {
        return Arrays.stream(BoardSearchType.values())
            .filter(type -> type.name.equals(name))
            .findAny()
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s] 이름의 타입은 존재하지 않습니다.", name)));
    }
}
