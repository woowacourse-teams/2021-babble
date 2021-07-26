package gg.babble.babble.dto;

import gg.babble.babble.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
