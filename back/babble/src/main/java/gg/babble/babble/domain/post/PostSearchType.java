package gg.babble.babble.domain.post;

import gg.babble.babble.domain.repository.PostRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Getter;

@Getter
public enum PostSearchType {
    TITLE("title", "제목", PostRepository::findByTitleLikeAndDeletedFalseOrderByCreatedAtDesc),
    TITLE_AND_CONTENT("titleAndContent", "제목 + 내용", PostRepository::findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAtDesc),
    AUTHOR("author", "작성자", PostRepository::findByAccount_NicknameLikeAndDeletedFalseOrderByCreatedAtDesc),
    ALL("all", "제목 + 내용 + 작성자", PostRepository::findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAt);

    private static final Map<String, PostSearchType> POST_SEARCH_TYPE_MAP = new HashMap<>();

    static {
        for (PostSearchType postSearchType : values()) {
            POST_SEARCH_TYPE_MAP.put(postSearchType.name, postSearchType);
        }
    }

    private final String type;
    private final String name;
    private final BiFunction<PostRepository, String, List<Post>> biFunction;

    PostSearchType(final String type, final String name, final BiFunction<PostRepository, String, List<Post>> biFunction) {
        this.type = type;
        this.name = name;
        this.biFunction = biFunction;
    }

    public static PostSearchType getPostSearchTypeByName(final String name) {
        if (!POST_SEARCH_TYPE_MAP.containsKey(name)) {
            throw new BabbleNotFoundException(String.format("[%s] 이름의 타입은 존재하지 않습니다.", name));
        }
        return POST_SEARCH_TYPE_MAP.get(name);
    }

    public List<Post> compose(final PostRepository postRepository, final String keyword) {
        return biFunction.apply(postRepository, String.format("%s%s%s", "%", keyword.toLowerCase(), "%"));
    }
}
