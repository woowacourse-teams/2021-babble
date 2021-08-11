package gg.babble.babble.dto.response;

import gg.babble.babble.domain.admin.Administrator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorResponse {

    private Long id;
    private String ip;
    private String name;

    public static AdministratorResponse from (final Administrator administrator) {
        return new AdministratorResponse(administrator.getId(), administrator.getIp().getValue(), administrator.getName());
    }
}
