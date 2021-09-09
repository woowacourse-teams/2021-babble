package gg.babble.babble.dto.response;

import gg.babble.babble.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionsResponse {

    private UserResponse host;
    private List<UserResponse> guests;

    public static SessionsResponse empty() {
        return new SessionsResponse(null, null);
    }

    public static SessionsResponse of(final User host, final List<User> guests) {
        List<UserResponse> guestResponses = guests.stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());

        return new SessionsResponse(UserResponse.from(host), guestResponses);
    }
}
