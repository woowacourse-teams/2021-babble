package gg.babble.babble.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameImageResponse {

    private final Long gameId;
    private final String image;
}
