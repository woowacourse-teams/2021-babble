package gg.babble.babble.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserListUpdateResponse {

    private final UserResponse host;
    private final List<UserResponse> guests;

    public static UserListUpdateResponse empty() {
        return new UserListUpdateResponse(null, null);
    }
}
