package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.parsing.ParseAttempt;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RandomNaturesRequirement extends AbstractPokemonRequirement<List<Nature>> {

    private static final Set<String> KEYS = Sets.newHashSet("randomnatures");

    private List<Nature> natures;

    public RandomNaturesRequirement() {
        super(KEYS);
    }

    public RandomNaturesRequirement(List<Nature> natures) {
        this();

        this.natures = natures;
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

        String[] natures = args[1].split(",");
        int amount = UtilParse.parseInteger(args[2]).orElse(1);

        if (amount > natures.length) {
            return ParseAttempt.error("Invalid amount specified. It cannot be more than the number of natures given");
        }

        List<String> randomNatures = Lists.newArrayList();

        if (amount == natures.length) {
            randomNatures.addAll(Arrays.asList(natures));
        } else {
            while (randomNatures.size() < amount) {
                randomNatures.add(UtilRandom.getRandomElementExcluding(natures, randomNatures.toArray(new String[0])));
            }
        }

        return this.createInstance(randomNatures.stream().map(Nature::natureFromString).toList())
                .map(Collections::singletonList);
    }

    @Override
    public ParseAttempt<Requirement<Pokemon, PixelmonEntity, List<Nature>>> createInstance(List<Nature> natures) {
        return ParseAttempt.success(new RandomNaturesRequirement(natures));
    }

    @Override
    public boolean isDataMatch(Pokemon pixelmon) {
        return this.natures.contains(pixelmon.getNature());
    }

    @Override
    public void applyData(Pokemon pixelmon) {
        pixelmon.setNature(UtilRandom.getRandomElement(this.natures));
    }

    @Override
    public List<Nature> getValue() {
        return this.natures;
    }
}
