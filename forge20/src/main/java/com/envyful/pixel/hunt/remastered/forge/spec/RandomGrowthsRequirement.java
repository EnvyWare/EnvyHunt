package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RandomGrowthsRequirement extends AbstractPokemonRequirement<List<EnumGrowth>> {

    private static final Set<String> KEYS = Sets.newHashSet("randomgrowths");

    private List<EnumGrowth> growths;

    public RandomGrowthsRequirement() {
        super(KEYS);
    }

    public RandomGrowthsRequirement(List<EnumGrowth> growths) {
        this();

        this.growths = growths;
    }

    @Override
    public ParseAttempt<List<Requirement<Pokemon, PixelmonEntity, ?>>> create(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return ParseAttempt.error("No key found");
        }

        String[] args = spec.split(":");

        if (args.length != 3) {
            return ParseAttempt.error("No key found");
        }

        String[] growths = args[1].split(",");
        int amount = UtilParse.parseInteger(args[2]).orElse(1);

        if (amount > growths.length) {
            return ParseAttempt.error("Invalid amount specified. It cannot be more than the number of growths given");
        }

        List<String> randomGrowths = Lists.newArrayList();

        if (amount == growths.length) {
            randomGrowths.addAll(Arrays.asList(growths));
        } else {
            while (randomGrowths.size() < amount) {
                randomGrowths.add(UtilRandom.getRandomElementExcluding(growths, randomGrowths.toArray(new String[0])));
            }
        }

        return this.createInstance(randomGrowths.stream().map(EnumGrowth::getGrowthFromString).toList())
                .map(Collections::singletonList);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, List<EnumGrowth>>> createInstance(List<EnumGrowth> growths) {
        return ParseAttempt.success(new RandomGrowthsRequirement(growths));
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        return this.growths.contains(pixelmon.getGrowth());
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        pixelmon.setGrowth(UtilRandom.getRandomElement(this.growths));
    }

    @Override
    public List<EnumGrowth> getValue() {
        return this.growths;
    }
}
