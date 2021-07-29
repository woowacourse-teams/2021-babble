package gg.babble.babble.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequest {

    @NotNull(message = "태그 Id는 Null 일 수 없습니다.")
    private Long id;
}
