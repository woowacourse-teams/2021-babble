package gg.babble.babble.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private Long roomId;
    private MessageResponse messageResponse;
}
