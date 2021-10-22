package gg.babble.babble.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntryResponse {

    private Long roomId;
    private SessionsResponse sessionsResponse;

}
