package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;

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
    public List<Requirement<Pokemon, PixelmonEntity, ?>> createSimple(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return Collections.emptyList();
        }

        String[] args = spec.split(key + ":");

        if (args.length != 3) {
            return Collections.emptyList();
        }

        String[] growths = args[1].split(",");
        int amount = UtilParse.parseInteger(args[2]).orElse(1);

        if (amount > growths.length) {
            return Collections.emptyList();
        }

        List<EnumGrowth> randomGrowths = Lists.newArrayList();

        if (amount == growths.length) {
            for (String nature : growths) {
                randomGrowths.add(EnumGrowth.getGrowthFromString(nature));
            }
        } else {
            while (randomGrowths.size() < amount) {
                randomGrowths.add(EnumGrowth.getGrowthFromString(UtilRandom.getRandomElement(growths)));
            }
        }

        return Collections.singletonList(this.createInstance(randomGrowths));
    }

    @Override
    public Requirement<Pokemon, PixelmonEntity, List<EnumGrowth>> createInstance(List<EnumGrowth> growths) {
        return new RandomGrowthsRequirement(growths);
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
