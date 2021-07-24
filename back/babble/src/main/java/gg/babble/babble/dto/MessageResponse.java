package gg.babble.babble.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private UserResponse user;
    private String content;

    public static MessageResponse of(final UserResponse userResponse, final String content) {
        return MessageResponse.builder()
            .user(userResponse)
            .content(content)
            .build();
    }
}