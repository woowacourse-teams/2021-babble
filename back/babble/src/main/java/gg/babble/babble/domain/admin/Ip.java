package gg.babble.babble.domain.admin;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Ip {

    public static final String IP_REGEXP = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);

    @Column(name = "ip", unique = true)
    @NotNull
    private String value;

    public Ip(final String value) {
        validateToConstruct(value);
        this.value = value;
    }

    private void validateToConstruct(final String value) {
        if (!IP_PATTERN.matcher(value).matches()) {
            throw new BabbleIllegalArgumentException(String.format("%s는 IP 형식에 맞지 않습니다.", value));
        }
    }
}
