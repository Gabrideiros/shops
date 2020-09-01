package me.gabrideiros.lojas.services;

public interface Service<T> {

    void insert(T t);

    void update(T t);

    void delete(T t);

    void load();

}
