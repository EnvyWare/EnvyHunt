package com.xpgaming.pixelhunt.listener;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.xpgaming.pixelhunt.PixelHuntForge;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.hunt.PixelHuntFactory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PokemonCaptureListener {

    private final PixelHuntForge main;

    public PokemonCaptureListener(PixelHuntForge main) {
        this.main = main;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPokemonCaught(CaptureEvent.SuccessfulCapture event) {
        Pokemon caught = event.getPokemon().getPokemonData();
        EntityPlayerMP player = event.player;

        for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
            if (hunt.isBeingHunted(caught)) {
                hunt.rewardCatch(player, caught);
                hunt.generatePokemon();
                break;
            }
        }
    }
}
