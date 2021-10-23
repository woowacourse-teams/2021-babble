package gg.babble.babble.dto.request.post;

import static gg.babble.babble.domain.post.Account.MAX_PASSWORD_LENGTH;
import static gg.babble.babble.domain.post.Account.MIN_PASSWORD_LENGTH;

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
public class PostDeleteRequest {

    @NotNull(message = "게시글 id 가 비어있습니다.")
    private Long id;

    @NotNull(message = "해당 게시글의 비밀번호가 입력되지 않았습니다.")
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    private String password;
}
