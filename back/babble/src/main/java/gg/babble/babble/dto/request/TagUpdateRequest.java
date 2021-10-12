package gg.babble.babble.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateRequest {

    @NotNull
    private String name;
    @NotNull
    private List<String> alternativeNames;
}
