package com.envyful.pixel.hunt.remastered.forge.spec;

import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class MaxIVRequirement extends AbstractBooleanPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet("maxivs");

    public MaxIVRequirement() {
        super(KEYS);
    }

    public MaxIVRequirement(boolean value) {
        super(KEYS, value);
    }


    @Override
    public Requirement<Pokemon, PixelmonEntity, Boolean> createInstance(Boolean aBoolean) {
        return new MaxIVRequirement(aBoolean);
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        for (int i : pokemon.getIVs().getArray()) {
            if (i != 31 && !this.value) {
                return true;
            }
        }

        return this.value;
    }

    @Override
    public void applyData(Pokemon pokemon) {
        if (this.value) {
            for (BattleStatsType battleStatsType : BattleStatsType.EV_IV_STATS) {
                pokemon.getIVs().setStat(battleStatsType, 31);
            }
        }
    }
}
