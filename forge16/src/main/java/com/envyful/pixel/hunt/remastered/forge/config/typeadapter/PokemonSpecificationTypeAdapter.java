package com.envyful.pixel.hunt.remastered.forge.config.typeadapter;

import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class PokemonSpecificationTypeAdapter extends ScalarSerializer<PokemonSpecification> {
    public PokemonSpecificationTypeAdapter() {
        super(PokemonSpecification.class);
    }

    @Override
    public PokemonSpecification deserialize(Type type, Object obj) throws SerializationException {
        return PokemonSpecificationProxy.create(obj.toString());
    }

    @Override
    protected Object serialize(PokemonSpecification item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
