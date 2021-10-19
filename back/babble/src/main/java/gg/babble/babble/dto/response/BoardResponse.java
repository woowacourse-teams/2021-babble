package gg.babble.babble.dto.response;

import gg.babble.babble.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String nickname;
    private String createdAt;
    private String updatedAt;

    private long view;
    private long like;

    public static BoardResponse from(final Board board) {
        return new BoardResponse(board.getId(), board.title(), board.content(), board.category(), board.nickname(), board.createdAt(), board.updatedAt(),
            board.view(), board.like());
    }
}
