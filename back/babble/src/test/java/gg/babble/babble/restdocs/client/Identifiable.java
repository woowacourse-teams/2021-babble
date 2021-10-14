package gg.babble.babble.restdocs.client;

public interface Identifiable<T, ID> {

    ID getId(T object);
}
