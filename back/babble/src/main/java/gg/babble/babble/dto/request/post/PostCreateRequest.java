package gg.babble.babble.dto.request.post;

import static gg.babble.babble.domain.post.Account.MAX_PASSWORD_LENGTH;
import static gg.babble.babble.domain.post.Account.MIN_PASSWORD_LENGTH;

import gg.babble.babble.domain.post.Post;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {

    @NotNull(message = "게시글 제목은 공백일 수 없습니다.")
    private String title;

    @NotNull(message = "내용이 작성되지 않았습니다.")
    private String content;

    @NotNull(message = "카테고리가 선택되지 않았습니다.")
    private String category;

    @NotNull(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    private String password;

    public Post toEntity() {
        return new Post(title, content, category, nickname, password);
    }
}
