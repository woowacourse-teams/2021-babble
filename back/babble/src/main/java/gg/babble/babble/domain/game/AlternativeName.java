package gg.babble.babble.domain.game;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlternativeName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "대안 이름은 Null 일 수 없습니다.")
    private String name;
    @ManyToOne
    @JoinColumn(name = "game_id")
    @NotNull(message = "게임은 Null 일 수 없습니다.")
    private Game game;

    public AlternativeName(final Long id, final String name, final Game game) {
        this.id = id;
        this.name = name;
        setGame(game);
    }

    public AlternativeName(final String name, final Game game) {
        this(null, name, game);
    }

    public void setGame(final Game game) {
        this.game = game;

        if (game.hasNotName(name)) {
            game.addAlternativeName(this);
        }
    }
}
