package gg.babble.babble.domain;

import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sessions {

    @OneToMany(mappedBy = "room")
    private final List<Session> sessions = new ArrayList<>();

    public void add(final Session session) {
        sessions.add(session);
    }

    public void remove(final Session session) {
        sessions.remove(session);
    }

    public List<User> sortedUsersByEnteredTime() {
        if (sessions.isEmpty()) {
            throw new BabbleIllegalStatementException("유저가 존재하지 않습니다.");
        }

        return sessions.stream()
            .sorted(Comparator.comparing(Session::getCreatedDate))
            .map(Session::getUser)
            .collect(Collectors.toList());
    }

    public int headCount() {
        return sessions.size();
    }

    public boolean isEmpty() {
        return sessions.isEmpty();
    }

    public boolean contains(final Session session) {
        return sessions.contains(session);
    }

    public boolean noContains(final Session session) {
        return !sessions.contains(session);
    }
}
