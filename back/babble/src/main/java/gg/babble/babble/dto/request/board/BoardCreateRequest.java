package gg.babble.babble.dto.request.board;

import static gg.babble.babble.domain.board.Account.MAX_PASSWORD_LENGTH;
import static gg.babble.babble.domain.board.Account.MIN_PASSWORD_LENGTH;

import gg.babble.babble.domain.board.Board;
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
public class BoardCreateRequest {

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

    public Board toEntity() {
        return new Board(title, content, category, nickname, password);
    }
}
