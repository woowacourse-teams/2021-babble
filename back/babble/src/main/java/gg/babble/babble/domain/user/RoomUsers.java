package gg.babble.babble.domain.user;

import gg.babble.babble.exception.BabbleIllegalStatementException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class RoomUsers {

    @OneToMany(mappedBy = "room")
    private final List<User> users = new ArrayList<>();

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }


    public User host() {
        if (users.isEmpty()) {
            throw new BabbleIllegalStatementException("유저가 존재하지 않습니다.");
        }
        return users.get(0);
    }

    public List<User> guests() {
        if (users.isEmpty()) {
            throw new BabbleIllegalStatementException("유저가 존재하지 않습니다.");
        }
        return users.subList(1, users.size());
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean hasUser(final User user) {
        return users.contains(user);
    }
}
