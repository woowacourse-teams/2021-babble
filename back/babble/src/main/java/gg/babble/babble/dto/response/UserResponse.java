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
public class UserResponse {

    private Long id;
    private String nickname;
    private String avatar;

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(), user.getNickname(), user.getAvatar());
    }
}
