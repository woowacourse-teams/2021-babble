package gg.babble.babble.dto.request;

import gg.babble.babble.domain.admin.Ip;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorRequest {

    @NotNull
    @Pattern(regexp = Ip.IP_REGEXP)
    private String ip;

    @NotNull
    private String name;
}
