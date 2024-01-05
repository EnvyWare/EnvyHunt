package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.Pair;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class IVPercentageRangeRequirement extends AbstractPokemonRequirement<Pair<Integer, Integer>> {

    private static final Set<String> KEYS = Sets.newHashSet("ivpercentrange");

    private int min;
    private int max;

    public IVPercentageRangeRequirement() {
        super(KEYS);
    }

    public IVPercentageRangeRequirement(int min, int max) {
        super(KEYS);
    }

    @Override
    public ParseAttempt<List<Requirement<Pokemon, PixelmonEntity, ?>>> create(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return ParseAttempt.error("No key found");
        }

        String[] args = spec.split(key + ":");

        if (args.length != 2) {
            return ParseAttempt.error("Invalid range specified");
        }

        String[] range = args[1].split("-");

        if (range.length != 2) {
            return ParseAttempt.error("Invalid range specified");
        }

        int min = UtilParse.parseInteger(range[0]).orElse(0);
        int max = UtilParse.parseInteger(range[1]).orElse(100);

        return this.createInstance(Pair.of(min, max))
                .map(Collections::singletonList);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, Pair<Integer, Integer>>> createInstance(Pair<Integer, Integer> percentage) {
        return ParseAttempt.success(new IVPercentageRangeRequirement(percentage.getA(), percentage.getB()));
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        var percentage = pixelmon.getIVs().getPercentage(1);
        return percentage >= this.min && percentage <= this.max;
    }

    @Override
    public Pair<Integer, Integer> getValue() {
        return Pair.of(this.min, this.max);
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        int iv = (int) ((UtilRandom.randomInteger(this.min, this.max) / 100.00) * 31);
        pixelmon.getIVs().fillFromArray(iv, iv, iv, iv, iv, iv);
    }
}
