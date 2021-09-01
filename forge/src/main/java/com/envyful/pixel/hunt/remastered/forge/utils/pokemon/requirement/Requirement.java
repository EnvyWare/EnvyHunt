package com.envyful.pixel.hunt.remastered.forge.utils.pokemon.requirement;

public interface Requirement<T> {

    T get();

    boolean fits(T data);

}
