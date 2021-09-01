package com.xpgaming.pixelhunt.utils.pokemon.requirement.impl;

import com.xpgaming.pixelhunt.utils.math.UtilRandom;

public class RandomMinimumIntegerRequirement extends MinimumIntegerRequirement {

    public RandomMinimumIntegerRequirement(int min, int max) {
        super(UtilRandom.getRandomInteger(min, max));
    }
}
