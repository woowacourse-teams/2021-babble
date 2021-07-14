package gg.babble.babble.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEnterResponse {
    private UserResponse host;
    private List<UserResponse> guests;
}
