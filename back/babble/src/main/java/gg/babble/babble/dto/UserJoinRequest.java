package gg.babble.babble.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    @NotNull(message = "userId는 비어있을 수 없습니다.")
    @Positive(message = "userId는 음수일 수 없습니다.")
    private Long userId;

    @NotBlank(message = "sessionId는 비어있을 수 없습니다.")
    private String sessionId;
}
