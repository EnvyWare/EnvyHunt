package com.envyful.pixel.hunt.remastered.forge.task;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.neoforge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;
import java.util.List;

public class ParticleDisplayTask extends LazyListener {

    private static ParticleDisplayTask instance;

    private final List<PixelmonEntity> huntPokemon = Lists.newArrayList();

    private int currentTick = 0;

    public ParticleDisplayTask() {
        super();

        instance = this;
    }

    public static void addPokemon(PixelmonEntity pixelmon) {
        instance.huntPokemon.add(pixelmon);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        ++this.currentTick;

        if (this.currentTick % 10 != 0) {
            return;
        }

        Iterator<PixelmonEntity> iterator = this.huntPokemon.iterator();
        UtilConcurrency.runAsync(() -> {
            while (iterator.hasNext()) {
                PixelmonEntity pixelmon = iterator.next();

                if (pixelmon == null || !pixelmon.isAlive() || pixelmon.hasOwner()) {
                    iterator.remove();
                    continue;
                }

                for (var hunt : EnvyHunt.getConfig().getHunts()) {
                    if (hunt.shouldPlayParticles() && hunt.matchesDisplay(pixelmon)) {
                        ((ServerLevel) pixelmon.level()).sendParticles(hunt.getParticles(),
                                pixelmon.getX(),
                                pixelmon.getY(),
                                pixelmon.getZ(),
                                1,
                                RandomHelper.getLegacyRandom().nextDouble() - 0.5,
                                RandomHelper.getLegacyRandom().nextDouble() - 0.5,
                                RandomHelper.getLegacyRandom().nextDouble() - 0.5,
                                1
                        );
                    }
                }
            }

            for (var hunt : EnvyHunt.getConfig().getHunts()) {
                if (hunt.hasTimedOut()) {
                    hunt.end();
                }
            }
        });
    }
}
