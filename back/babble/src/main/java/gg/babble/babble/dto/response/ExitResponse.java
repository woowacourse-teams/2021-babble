package gg.babble.babble.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ExitResponse {

    private final Long roomId;
    private final SessionsResponse sessionsResponse;

}
