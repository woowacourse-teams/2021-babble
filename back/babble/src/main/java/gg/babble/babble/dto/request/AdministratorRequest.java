package gg.babble.babble.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorRequest {

    @NotNull
    private String ip;

    @NotNull
    private String name;
}
