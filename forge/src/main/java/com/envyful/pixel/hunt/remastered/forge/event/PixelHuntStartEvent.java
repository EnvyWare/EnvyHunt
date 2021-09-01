package com.envyful.pixel.hunt.remastered.forge.event;

import com.envyful.api.reforged.pixelmon.PokemonSpec;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 *
 * Represents a {@link PixelHunt} being started
 *
 */
@Cancelable
public class PixelHuntStartEvent extends Event {

    private final PixelHunt hunt;
    private PokemonSpec generatedPokemon;

    public PixelHuntStartEvent(PixelHunt hunt, PokemonSpec generatedPokemon) {
        this.hunt = hunt;
        this.generatedPokemon = generatedPokemon;
    }

    public PixelHunt getHunt() {
        return this.hunt;
    }

    public PokemonSpec getGeneratedPokemon() {
        return this.generatedPokemon;
    }

    public void setGeneratedPokemon(PokemonSpec generatedPokemon) {
        this.generatedPokemon = generatedPokemon;
    }
}
