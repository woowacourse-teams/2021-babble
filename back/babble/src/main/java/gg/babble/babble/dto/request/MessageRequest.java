package gg.babble.babble.dto.request;


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
public class MessageRequest {

    @NotNull(message = "userId는 비어있을 수 없습니다.")
    @Positive(message = "userId는 음수일 수 없습니다.")
    private Long userId;

    @NotNull(message = "채팅 메시지는 Null 일 수 없습니다.")
    private String content;

    @NotNull(message = "채팅 타입은 Null 일 수 없습니다.")
    private String type;
}
