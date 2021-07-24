package gg.babble.babble.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequest {

    @NotNull(message = "태그 이름은 Null 일 수 없습니다.")
    private String name;
}
