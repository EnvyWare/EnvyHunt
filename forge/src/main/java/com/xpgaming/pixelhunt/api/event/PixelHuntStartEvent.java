package com.xpgaming.pixelhunt.api.event;

import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.utils.pokemon.PokemonSpec;
import net.minecraftforge.fml.common.eventhandler.Event;

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
