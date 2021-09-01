package com.xpgaming.pixelhunt.utils.pokemon.requirement.impl;

import com.xpgaming.pixelhunt.utils.pokemon.requirement.Requirement;

public class MinimumIntegerRequirement implements Requirement<Integer> {

    private final int requirement;

    public MinimumIntegerRequirement(int requirement) {
        this.requirement = requirement;
    }

    @Override
    public Integer get() {
        return this.requirement;
    }

    @Override
    public boolean fits(Integer data) {
        if (data == null) {
            return false;
        }

        return data >= this.requirement;
    }
}
