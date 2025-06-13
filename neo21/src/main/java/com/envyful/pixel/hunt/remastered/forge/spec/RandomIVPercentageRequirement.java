package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RandomIVPercentageRequirement extends AbstractIntegerPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet("randomivpercent");

    public RandomIVPercentageRequirement() {
        super(KEYS, 0);
    }

    public RandomIVPercentageRequirement(int percentage) {
        super(KEYS, 0, percentage);
    }

    @Override
    public ParseAttempt<List<Requirement<Pokemon, PixelmonEntity, ?>>> create(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return ParseAttempt.error("No key found");
        }

        String[] args = spec.split(key + ":");

        if (args.length != 2) {
            return ParseAttempt.error("No key found");
        }

        String[] range = args[1].split("-");

        if (range.length != 2) {
            return ParseAttempt.error("Range must be formatted like so: <min>-<max> (i.e. 1-100)");
        }

        int min = UtilParse.parseInt(range[0]).orElse(0);
        int max = UtilParse.parseInt(range[1]).orElse(100);

        return this.createInstance(UtilRandom.randomInteger(min, max)).map(Collections::singletonList);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Integer>> createInstance(Integer percentage) {
        return ParseAttempt.success(new RandomIVPercentageRequirement(percentage));
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        return pixelmon.getIVs().getPercentage(1) == this.value;
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        int iv = (int) ((this.value / 100.00) * 31);
        pixelmon.getIVs().fillFromArray(iv, iv, iv, iv, iv, iv);
    }
}
