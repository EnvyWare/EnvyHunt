package com.envyful.pixel.hunt.remastered.forge.event;

import com.envyful.api.reforged.pixelmon.PokemonSpec;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 *
 * Represents a player winning a {@link PixelHunt} by catching the correct pokemon
 *
 */
@Cancelable
public class PixelHuntWonEvent extends Event {

    private final PixelHunt hunt;
    private final ServerPlayerEntity player;
    private final Pokemon caughtPokemon;
    private final PokemonSpec caughtSpec;

    public PixelHuntWonEvent(PixelHunt hunt, ServerPlayerEntity player, Pokemon caughtPokemon, PokemonSpec caughtSpec) {
        this.hunt = hunt;
        this.player = player;
        this.caughtPokemon = caughtPokemon;
        this.caughtSpec = caughtSpec;
    }

    public PixelHunt getHunt() {
        return this.hunt;
    }

    public ServerPlayerEntity getPlayer() {
        return this.player;
    }

    public Pokemon getCaughtPokemon() {
        return this.caughtPokemon;
    }

    public PokemonSpec getCaughtSpec() {
        return this.caughtSpec;
    }
}
