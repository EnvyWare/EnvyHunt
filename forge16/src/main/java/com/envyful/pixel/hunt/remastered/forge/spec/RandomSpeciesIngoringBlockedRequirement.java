package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RandomSpeciesIngoringBlockedRequirement extends AbstractPokemonRequirement<Species> {

    private static final Set<String> KEYS = Sets.newHashSet("randomnonblockedspecies");
    private Species species;

    public RandomSpeciesIngoringBlockedRequirement() {
        super(KEYS);
    }

    public RandomSpeciesIngoringBlockedRequirement(Species species) {
        this();

        this.species = species;
    }

    @Override
    public List<Requirement<Pokemon, PixelmonEntity, ?>> createSimple(String key, String spec) {
        if (!spec.startsWith(key)) {
            return Collections.emptyList();
        }

        Species randomSpecies = PixelmonSpecies.getRandomSpecies();

        while (EnvyHunt.getConfig().getBlockedSpecies().contains(randomSpecies.getName())) {
            randomSpecies = PixelmonSpecies.getRandomSpecies();
        }

        return Collections.singletonList(new RandomSpeciesIngoringBlockedRequirement(randomSpecies));
    }

    @Override
    public Requirement<Pokemon, PixelmonEntity, Species> createInstance(Species value) {
        return new RandomSpeciesIngoringBlockedRequirement(value);
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        return pixelmon.getSpecies().is(this.species);
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        pixelmon.setSpecies(this.species, false);
    }

    @Override
    public Species getValue() {
        return this.species;
    }
}
