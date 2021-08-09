package gg.babble.babble.dto.request;

import gg.babble.babble.domain.user.User;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull
    @Size(min = User.MIN_NICKNAME_LENGTH, max = User.MAX_NICKNAME_LENGTH)
    private String nickname;
}
