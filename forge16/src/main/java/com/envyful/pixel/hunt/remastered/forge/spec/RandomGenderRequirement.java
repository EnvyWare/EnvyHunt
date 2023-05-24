package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomGenderRequirement extends AbstractPokemonRequirement<Gender> {

    private static final Set<String> KEYS = Sets.newHashSet("randomgender");

    private Gender gender;

    public RandomGenderRequirement() {
        super(KEYS);
    }

    public RandomGenderRequirement(Gender gender) {
        this();

        this.gender = gender;
    }

    @Override
    public List<Requirement<Pokemon, PixelmonEntity, ?>> createSimple(String key, String spec) {
        if (!spec.startsWith(key)) {
            return Collections.emptyList();
        }

        return Collections.singletonList(this.createInstance(UtilRandom.getRandomElement(Gender.values())));
    }

    @Override
    public Requirement<Pokemon, PixelmonEntity, Gender> createInstance(Gender gender) {
        return new RandomGenderRequirement(gender);
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        if (pixelmon.getForm().isGenderless() && !Objects.equals(this.gender, Gender.NONE)) {
            return true;
        }

        if (!pixelmon.getForm().isGenderless() && Objects.equals(this.gender, Gender.NONE)) {
            return true;
        }

        return Objects.equals(this.gender, pixelmon.getGender());
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        pixelmon.setGender(this.gender);
    }

    @Override
    public Gender getValue() {
        return this.gender;
    }
}
