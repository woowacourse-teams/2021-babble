package gg.babble.babble.dto.response;

import gg.babble.babble.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UserResponse user;
    private String content;
    private String type;

    public static MessageResponse from(final User user, final String content, final String type) {
        return new MessageResponse(UserResponse.from(user), content, type);
    }
}
