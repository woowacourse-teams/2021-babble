package gg.babble.babble.domain.board;

import gg.babble.babble.domain.repository.BoardRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import lombok.Getter;

@Getter
public enum BoardSearchType {
    TITLE("title", "제목", BoardRepository::findByPost_TitleLikeAndDeletedFalseOrderByCreatedAtDesc),
    TITLE_AND_CONTENT("titleAndContent", "제목 + 내용", BoardRepository::findByPost_TitleLikeOrPost_ContentLikeAndDeletedFalseOrderByCreatedAtDesc),
    AUTHOR("author", "작성자", BoardRepository::findByAccount_NicknameLikeAndDeletedFalseOrderByCreatedAtDesc),
    ALL("all", "제목 + 내용 + 작성자", BoardRepository::findByPost_TitleLikeOrPost_ContentLikeAndDeletedFalseOrderByCreatedAt);

    private final String type;
    private final String name;
    private final BiFunction<BoardRepository, String, List<Board>> biFunction;

    BoardSearchType(final String type, final String name, final BiFunction<BoardRepository, String, List<Board>> biFunction) {
        this.type = type;
        this.name = name;
        this.biFunction = biFunction;
    }

    public static BoardSearchType from(final String type) {
        return Arrays.stream(BoardSearchType.values())
            .filter(boardSearchType -> boardSearchType.type.equals(type))
            .findAny()
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s] 이름의 타입은 존재하지 않습니다.", type)));
    }

    public List<Board> compose(final BoardRepository boardRepository, final String keyword) {
        return biFunction.apply(boardRepository, String.format("%s%s%s", "%", keyword.toLowerCase(), "%"));
    }
}
