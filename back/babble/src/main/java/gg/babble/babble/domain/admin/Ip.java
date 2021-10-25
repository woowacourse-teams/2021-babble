package gg.babble.babble.domain.admin;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;

@Getter
@NoArgsConstructor
@Embeddable
public class Ip {

    private static final InetAddressValidator IP_VALIDATOR = InetAddressValidator.getInstance();

    @Column(name = "ip", unique = true)
    @NotNull
    private String value;

    public Ip(final String value) {
        validateToConstruct(value);
        this.value = value;
    }

    private void validateToConstruct(final String value) {
        if (!IP_VALIDATOR.isValid(value)) {
            throw new BabbleIllegalArgumentException(String.format("%s는 IP 형식에 맞지 않습니다.", value));
        }
    }
}
