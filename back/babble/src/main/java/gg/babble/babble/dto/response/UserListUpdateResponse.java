package gg.babble.babble.dto.response;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
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

    public static UserListUpdateResponse of(final User host, final List<User> guests) {
        List<UserResponse> guestResponses = guests.stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());

        return new UserListUpdateResponse(UserResponse.from(host), guestResponses);
    }
}
