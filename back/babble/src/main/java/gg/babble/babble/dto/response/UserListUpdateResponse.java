package gg.babble.babble.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserListUpdateResponse {

    private UserResponse host;
    private List<UserResponse> guests;

    public static UserListUpdateResponse empty() {
        return new UserListUpdateResponse(null, null);
    }
}
