package gg.babble.babble.dto;

import gg.babble.babble.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostResponseDto {

    private Long id;
    private String name;

    public static HostResponseDto from(final User host) {
        return HostResponseDto.builder()
                .id(host.getId())
                .name(host.getName())
                .build();
    }
}
