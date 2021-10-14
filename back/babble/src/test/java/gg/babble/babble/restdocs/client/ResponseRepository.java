package gg.babble.babble.restdocs.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseRepository<T, ID> {

    private final Map<ID, T> elements = new LinkedHashMap<>();
    private final Identifiable<T, ID> identifiable;

    public ResponseRepository(final Identifiable<T, ID> identifiable) {
        this.identifiable = identifiable;
    }

    public void add(final T response) {
        elements.put(identifiable.getId(response), response);
    }

    public ID getAnyId() {
        return elements.keySet().iterator().next();
    }

    public T get(final ID id) {
        return elements.get(id);
    }

    public List<ID> getAllIds() {
        return new ArrayList<>(elements.keySet());
    }

    public int getSize() {
        return elements.keySet().size();
    }
}
