package me.gabrideiros.lojas.utils;


import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

public abstract class Cache<T> {

    @Getter
    private final Set<T> elements = new HashSet<>();

    public boolean contains(T element) {
        return elements.contains(element);
    }

    public void addElement(T toAdd) {
        elements.add(toAdd);
    }

    public void removeElement(T toRemove) {
        elements.remove(toRemove);
    }

    public T get(Predicate<T> predicate) {
        for (T element : elements)
            if (predicate.test(element)) return element;

        return null;
    }

    public T getAndRemove(Predicate<T> predicate) {
        T element = get(predicate);
        if (element != null) removeElement(element);

        return element;
    }

    public T[] getAll(Predicate<T> predicate) {
        List<T> array = new LinkedList<>();

        for (T element : elements) {
            if (predicate.test(element)) array.add(element);
        }

        return (T[]) array.toArray();
    }

    public Optional<T> find(Predicate<T> predicate) {
        return Optional.ofNullable(get(predicate));
    }

    public Optional<T> findAndRemove(Predicate<T> predicate) {
        Optional<T> optional = find(predicate);
        optional.ifPresent(this::removeElement);

        return optional;
    }

    public Iterator<T> iterator() {
        return elements.iterator();
    }

    public int size() {
        return elements.size();
    }

}