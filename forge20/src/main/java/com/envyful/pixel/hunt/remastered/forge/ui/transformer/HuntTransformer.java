package com.envyful.pixel.hunt.remastered.forge.ui.transformer;

import com.envyful.api.text.parse.SimplePlaceholder;
import com.envyful.api.time.UtilTimeFormat;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.spec.*;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.requirement.impl.SpeciesRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HuntTransformer implements SimplePlaceholder {

    private final PixelHuntConfig.HuntConfig hunt;

    public HuntTransformer(PixelHuntConfig.HuntConfig hunt) {
        this.hunt = hunt;
    }

    @Override
    public String replace(String name) {
        var species = this.getValue(this.hunt.getRequirementSpecs(), SpeciesRequirement.class);

        if (species != null && species.isInitialized() && species.getValue().isPresent()) {
            name = name.replace("%species%", species.getValueUnsafe().getName())
                    .replace("%dex_number%", String.valueOf(species.getValueUnsafe().getDex()));
        }

        var value = this.getValue(this.hunt.getRequirementSpecs(), RandomSpeciesIngoringBlockedRequirement.class);

        if (value != null)  {
            name = name.replace("%species%", value.getName())
                    .replace("%dex_number%", String.valueOf(value.getDex()));
        }

        var ability = this.getValue(this.hunt.getRequirementSpecs(), RandomAbilityRequirement.class);

        if (ability != null) {
            name = name.replace("%ability%", ability.getLocalizedName());
        }

        var gender = this.getValue(this.hunt.getRequirementSpecs(), RandomGenderRequirement.class);

        if (gender != null) {
            name = name.replace("%gender%", gender.getLocalizedName());
        }

        var growths = this.getValue(this.hunt.getRequirementSpecs(), RandomGrowthsRequirement.class);

        if (growths != null) {
            for (int i = 0; i < growths.size(); i++) {
                name = name.replace("%growth_" + (i + 1) + "%", growths.get(i).getLocalizedName());
            }
        }

        var percentage = this.getValue(this.hunt.getRequirementSpecs(), RandomIVPercentageRequirement.class);

        if (percentage != null) {
            name = name.replace("%ivs%", String.valueOf(percentage));
        }

        var natures = this.getValue(this.hunt.getRequirementSpecs(), RandomNaturesRequirement.class);

        if (natures != null) {
            for (int i = 0; i < natures.size(); i++) {
                name = name.replace("%nature_" + (i + 1) + "%", natures.get(i).getLocalizedName());
            }
        }

        name = name.replace("%time%", UtilTimeFormat.getFormattedDuration((this.hunt.getCurrentStart() + TimeUnit.MINUTES.toMillis(this.hunt.getMaxDurationMinutes())) - System.currentTimeMillis()));

        return name;
    }

    private <T> T getValue(List<PokemonSpecification> specs,
                           Class<? extends Requirement<Pokemon, PixelmonEntity, T>> requirementClazz) {
        for (PokemonSpecification spec : specs) {
            T value = spec.getValue(requirementClazz).orElse(null);
            if (value == null) {
                continue;
            }

            return value;
        }

        return null;
    }
}
