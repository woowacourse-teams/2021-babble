package gg.babble.babble.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchResponse {

    private List<BoardResponse> results;
    private String keyword;
    private String type;

    public static BoardSearchResponse from(final List<BoardResponse> responses, final String keyword, final String type) {
        return new BoardSearchResponse(responses, keyword, type);
    }
}
