package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.*;

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
    public ParseAttempt<List<Requirement<Pokemon, PixelmonEntity, ?>>> create(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return ParseAttempt.error("No key found");
        }

        String[] args = spec.split(":");

        if (args.length != 2) {
            return ParseAttempt.error("No key found");
        }

        String[] abilities = args[1].split(",");
        String abilityName = UtilRandom.getRandomElement(abilities);

        if (abilityName == null) {
            return ParseAttempt.error("Error ability " + abilityName + " is not valid");
        }

        Ability randomAbility = AbilityRegistry.getAbility(abilityName.toLowerCase(Locale.ROOT)).orElse(null);

        if (randomAbility == null) {
            return ParseAttempt.error("Error ability " + abilityName + " is not valid");
        }

        return this.createInstance(randomAbility).map(Collections::singletonList);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Ability>> createInstance(Ability ability) {
        return ParseAttempt.success(new RandomAbilityRequirement(ability));
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
