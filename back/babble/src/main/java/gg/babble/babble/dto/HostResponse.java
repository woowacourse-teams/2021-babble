package gg.babble.babble.dto;

import gg.babble.babble.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostResponse {

    private Long id;
    private String name;

    public static HostResponse from(final User host) {
        return HostResponse.builder()
            .id(host.getId())
            .name(host.getName())
            .build();
    }
}
