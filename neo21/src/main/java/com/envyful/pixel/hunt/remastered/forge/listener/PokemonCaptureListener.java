package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public class PokemonCaptureListener {

    public PokemonCaptureListener() {
        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPokemonCaught(CaptureEvent.SuccessfulCapture event) {
        var caught = event.getPokemon();
        var player = event.getPlayer();

        UtilConcurrency.runAsync(() -> {
            for (var hunt : EnvyHunt.getConfig().getHunts()) {
                if (!hunt.isEnabled()) {
                    continue;
                }

                if (hunt.canParticipate(player) && hunt.matchesHunt(caught)) {
                    hunt.rewardHunt(player, caught);

                    if (!EnvyHunt.getConfig().isCatchesCountForMultipleHunts()) {
                        break;
                    }
                }
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPokemonCaught(CaptureEvent.SuccessfulRaidCapture event) {
        var caught = event.getRaidPokemon();
        var player = event.getPlayer();

        UtilConcurrency.runAsync(() -> {
            for (var hunt : EnvyHunt.getConfig().getHunts()) {
                if (!hunt.isEnabled()) {
                    continue;
                }

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
