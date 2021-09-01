package com.xpgaming.pixelhunt.api.event;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.utils.pokemon.PokemonSpec;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PixelHuntWonEvent extends Event {

    private final PixelHunt hunt;
    private final EntityPlayerMP player;
    private final Pokemon caughtPokemon;
    private final PokemonSpec caughtSpec;

    public PixelHuntWonEvent(PixelHunt hunt, EntityPlayerMP player, Pokemon caughtPokemon, PokemonSpec caughtSpec) {
        this.hunt = hunt;
        this.player = player;
        this.caughtPokemon = caughtPokemon;
        this.caughtSpec = caughtSpec;
    }

    public PixelHunt getHunt() {
        return this.hunt;
    }

    public EntityPlayerMP getPlayer() {
        return this.player;
    }

    public Pokemon getCaughtPokemon() {
        return this.caughtPokemon;
    }

    public PokemonSpec getCaughtSpec() {
        return this.caughtSpec;
    }
}
