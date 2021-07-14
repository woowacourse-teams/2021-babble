package gg.babble.babble.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User host;

    @NonNull
    @ManyToMany
    @JoinTable(name = "room_tag",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private List<Tag> tags;

    @CreatedDate
    private LocalDateTime createdDate;
}
