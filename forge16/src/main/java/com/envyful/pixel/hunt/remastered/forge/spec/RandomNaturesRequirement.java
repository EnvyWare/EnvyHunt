package com.envyful.pixel.hunt.remastered.forge.spec;

import com.envyful.api.math.UtilRandom;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<Requirement<Pokemon, PixelmonEntity, ?>> createSimple(String key, String spec) {
        if (!spec.startsWith(key + ":")) {
            return Collections.emptyList();
        }

        String[] args = spec.split(":");

        if (args.length != 3) {
            return Collections.emptyList();
        }

        String[] natures = args[1].split(",");
        int amount = UtilParse.parseInteger(args[2]).orElse(1);

        if (amount > natures.length) {
            return Collections.emptyList();
        }

        List<String> randomNatures = Lists.newArrayList();

        if (amount == natures.length) {
            randomNatures.addAll(Arrays.asList(natures));
        } else {
            while (randomNatures.size() < amount) {
                randomNatures.add(UtilRandom.getRandomElementExcluding(natures, randomNatures.toArray(new String[0])));
            }
        }

        return Collections.singletonList(this.createInstance(randomNatures.stream().map(Nature::natureFromString).collect(Collectors.toList())));
    }

    @Override
    public Requirement<Pokemon, PixelmonEntity, List<Nature>> createInstance(List<Nature> natures) {
        return new RandomNaturesRequirement(natures);
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
