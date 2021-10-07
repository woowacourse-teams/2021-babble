package gg.babble.babble.domain.admin;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE administrator SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Administrator {

    private final boolean deleted = Boolean.FALSE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Ip ip;
    @NotNull
    private String name;

    public Administrator(final String ip, final String name) {
        this(null, new Ip(ip), name);
    }

    public Administrator(final Long id, final Ip ip, final String name) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }
}
