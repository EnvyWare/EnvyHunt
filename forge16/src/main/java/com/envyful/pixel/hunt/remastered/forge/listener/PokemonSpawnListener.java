package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.config.HuntConfig;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PokemonSpawnListener extends LazyListener {

    public PokemonSpawnListener() {
        super();
    }

    @SubscribeEvent
    public void onPokemonSpawn(EntityJoinWorldEvent event) {
        UtilConcurrency.runAsync(() -> {
            Entity entity = event.getEntity();

            if(!(entity instanceof PixelmonEntity)) {
                return;
            }

            PixelmonEntity pixelmon = (PixelmonEntity) entity;

            for (HuntConfig hunt : EnvyHunt.getConfig().getHunts()) {
                if (!hunt.isEnabled()) {
                    continue;
                }

                if (hunt.matchesHunt(pixelmon)) {
                    if (hunt.shouldPlayParticles()) {
                        ParticleDisplayTask.addPokemon(pixelmon);
                    }
                }
            }
        });
    }
}
