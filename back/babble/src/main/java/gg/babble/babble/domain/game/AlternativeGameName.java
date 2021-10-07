package gg.babble.babble.domain.game;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE alternative_game_name SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlternativeGameName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "대안 이름은 Null 일 수 없습니다.")
    private String value;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Game game;

    private boolean isDeleted = false;

    public AlternativeGameName(final String value, final Game game) {
        this(null, value, game);
    }

    public AlternativeGameName(final Long id, final String value, final Game game) {
        this.id = id;
        this.value = value;
        setGame(game);
    }

    public void setGame(final Game game) {
        final Game previousGame = this.game;
        this.game = game;

        if (Objects.nonNull(previousGame) && previousGame.hasName(value)) {
            previousGame.removeAlternativeName(this);
        }

        game.addAlternativeName(this);
    }

    public void delete() {
        if (game.hasName(value)) {
            game.removeAlternativeName(this);
        }
        isDeleted = true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlternativeGameName that = (AlternativeGameName) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
