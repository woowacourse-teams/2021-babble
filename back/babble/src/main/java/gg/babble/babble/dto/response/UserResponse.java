package gg.babble.babble.dto.response;

import gg.babble.babble.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponse {

    private final Long id;
    private final String nickname;
    private final String avatar;

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(), user.getNickname(), user.getAvatar());
    }
}
