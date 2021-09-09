package gg.babble.babble.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExitResponse {

    private final Long roomId;
    private final SessionsResponse sessionsResponse;

}
