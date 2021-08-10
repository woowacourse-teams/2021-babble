package gg.babble.babble.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequest {

    @NotNull(message = "userId는 비어있을 수 없습니다.")
    @Positive(message = "userId는 음수일 수 없습니다.")
    private Long userId;

    @NotBlank(message = "sessionId는 비어있을 수 없습니다.")
    private String sessionId;
}
