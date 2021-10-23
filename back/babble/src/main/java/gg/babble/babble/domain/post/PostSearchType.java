package gg.babble.babble.domain.post;

import gg.babble.babble.domain.repository.PostRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import lombok.Getter;

@Getter
public enum PostSearchType {
    TITLE("title", "제목", PostRepository::findByTitleLikeAndDeletedFalseOrderByCreatedAtDesc),
    TITLE_AND_CONTENT("titleAndContent", "제목 + 내용", PostRepository::findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAtDesc),
    AUTHOR("author", "작성자", PostRepository::findByAccount_NicknameLikeAndDeletedFalseOrderByCreatedAtDesc),
    ALL("all", "제목 + 내용 + 작성자", PostRepository::findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAt);

    private final String type;
    private final String name;
    private final BiFunction<PostRepository, String, List<Post>> biFunction;

    PostSearchType(final String type, final String name, final BiFunction<PostRepository, String, List<Post>> biFunction) {
        this.type = type;
        this.name = name;
        this.biFunction = biFunction;
    }

    public static PostSearchType from(final String type) {
        return Arrays.stream(PostSearchType.values())
            .filter(postSearchType -> postSearchType.type.equals(type))
            .findAny()
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s] 이름의 타입은 존재하지 않습니다.", type)));
    }

    public List<Post> compose(final PostRepository postRepository, final String keyword) {
        return biFunction.apply(postRepository, String.format("%s%s%s", "%", keyword.toLowerCase(), "%"));
    }
}
