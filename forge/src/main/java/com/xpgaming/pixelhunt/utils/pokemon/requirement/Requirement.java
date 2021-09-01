package com.xpgaming.pixelhunt.utils.pokemon.requirement;

public interface Requirement<T> {

    T get();

    boolean fits(T data);

}
