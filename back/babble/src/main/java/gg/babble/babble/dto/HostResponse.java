package gg.babble.babble.dto;

import gg.babble.babble.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostResponse {

    private Long id;
    private String name;

    public static HostResponse from(final User host) {
        return new HostResponse(host.getId(), host.getName());
    }
}
