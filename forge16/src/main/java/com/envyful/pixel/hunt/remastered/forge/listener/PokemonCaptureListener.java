package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.config.HuntConfig;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PokemonCaptureListener {

    public PokemonCaptureListener() {
        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPokemonCaught(CaptureEvent.SuccessfulCapture event) {
        PixelmonEntity caught = event.getPokemon();
        ServerPlayerEntity player = event.getPlayer();

        UtilConcurrency.runAsync(() -> {
            for (HuntConfig hunt : EnvyHunt.getConfig().getHunts()) {
                if (hunt.canParticipate(player) && hunt.matchesHunt(caught)) {
                    hunt.rewardHunt(player, caught.getPokemon());

                    if (!EnvyHunt.getConfig().isCatchesCountForMultipleHunts()) {
                        break;
                    }
                }
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPokemonCaught(CaptureEvent.SuccessfulRaidCapture event) {
        Pokemon caught = event.getRaidPokemon();
        ServerPlayerEntity player = event.getPlayer();

        UtilConcurrency.runAsync(() -> {
            for (HuntConfig hunt : EnvyHunt.getConfig().getHunts()) {
                if (hunt.canParticipate(player) && hunt.matchesHunt(caught)) {
                    hunt.rewardHunt(player, caught);

                    if (!EnvyHunt.getConfig().isCatchesCountForMultipleHunts()) {
                        break;
                    }
                }
            }
        });
    }
}
