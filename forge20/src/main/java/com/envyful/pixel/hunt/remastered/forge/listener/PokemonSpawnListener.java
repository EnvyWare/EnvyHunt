package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PokemonSpawnListener extends LazyListener {

    public PokemonSpawnListener() {
        super();
    }

    @SubscribeEvent
    public void onPokemonSpawn(EntityJoinLevelEvent event) {
        UtilConcurrency.runAsync(() -> {
            var entity = event.getEntity();

            if(!(entity instanceof PixelmonEntity)) {
                return;
            }

            var pixelmon = (PixelmonEntity) entity;

            for (var hunt : EnvyHunt.getConfig().getHunts()) {
                if (!hunt.isEnabled()) {
                    continue;
                }

                if (hunt.matchesHunt(pixelmon)) {
                    if (hunt.shouldPlayParticles()) {
                        ParticleDisplayTask.addPokemon(pixelmon);
                    }

                    if (hunt.isCustomColour() && hunt.getColor() != null) {
                        pixelmon.setRenderColor(
                                hunt.getColor().getRed(),
                                hunt.getColor().getGreen(),
                                hunt.getColor().getBlue(),
                                hunt.getColor().getAlpha()
                        );
                    }
                }
            }
        });
    }
}
