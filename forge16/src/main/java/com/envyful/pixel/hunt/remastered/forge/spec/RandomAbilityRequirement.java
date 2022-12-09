package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomAbilityRequirement extends AbstractPokemonRequirement<Ability> {

    private static final Set<String> KEYS = Sets.newHashSet("randomability");

    private Ability ability;

    public RandomAbilityRequirement() {
        super(KEYS);
    }

    public RandomAbilityRequirement(Ability ability) {
        this();

        this.ability = ability;
    }

    @Override
    public List<Requirement<Pokemon, PixelmonEntity, ?>> createSimple(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return Collections.emptyList();
        }

        String[] args = spec.split(key + ":");

        if (args.length != 2) {
            return Collections.emptyList();
        }

        String[] abilities = args[1].split(",");
        String abilityName = UtilRandom.getRandomElement(abilities);

        if (abilityName == null) {
            return Collections.emptyList();
        }

        Ability randomAbility = AbilityRegistry.getAbility(abilityName).orElse(null);
        return randomAbility == null ? Collections.emptyList() : Collections.singletonList(this.createInstance(randomAbility));
    }

    @Override
    public Requirement<Pokemon, PixelmonEntity, Ability> createInstance(Ability ability) {
        return new RandomAbilityRequirement(ability);
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        return Objects.equals(this.ability, pixelmon.getAbility());
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        pixelmon.setAbility(this.ability.getNewInstance());
    }

    @Override
    public Ability getValue() {
        return this.ability;
    }
}
