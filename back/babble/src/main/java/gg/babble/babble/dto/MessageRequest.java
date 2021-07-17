package gg.babble.babble.dto;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    @NotNull(message = "userId는 비어있을 수 없습니다.")
    @Positive(message = "userId는 음수일 수 없습니다.")
    private Long userId;

    @NotNull(message = "채팅 메시지는 Null 일 수 없습니다.")
    private String content;
}
