package com.envyful.pixel.hunt.remastered.forge.utils.pokemon.requirement.impl;

import com.envyful.pixel.hunt.remastered.forge.utils.math.UtilRandom;

public class RandomMinimumIntegerRequirement extends MinimumIntegerRequirement {

    public RandomMinimumIntegerRequirement(int min, int max) {
        super(UtilRandom.getRandomInteger(min, max));
    }
}
