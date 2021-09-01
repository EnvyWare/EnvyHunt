package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.hunt.PixelHuntForgeFactory;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
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

        for (PixelHunt hunt : PixelHuntForgeFactory.getAllHunts()) {
            if (hunt.isBeingHunted(caught)) {
                hunt.rewardCatch(player, caught);
                hunt.generatePokemon();
                break;
            }
        }
    }
}
