package gg.babble.babble.dto;

import lombok.*;

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