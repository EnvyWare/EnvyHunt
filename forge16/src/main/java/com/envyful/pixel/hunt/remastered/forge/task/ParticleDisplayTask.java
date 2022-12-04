package com.envyful.pixel.hunt.remastered.forge.task;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public void onServerTick(TickEvent.ServerTickEvent event) {
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

                for (PixelHuntConfig.HuntConfig hunt : PixelHuntForge.getConfig().getHunts()) {
                    if (hunt.shouldPlayParticles() && hunt.matchesHunt(pixelmon)) {
                        ((ServerWorld) pixelmon.level).sendParticles(hunt.getParticles(),
                                pixelmon.getX(),
                                pixelmon.getY(),
                                pixelmon.getZ(),
                                1,
                                pixelmon.level.random.nextDouble() - 0.5,
                                pixelmon.level.random.nextDouble() - 0.5,
                                pixelmon.level.random.nextDouble() - 0.5,
                                1
                        );
                    }
                }
            }

            for (PixelHuntConfig.HuntConfig hunt : PixelHuntForge.getConfig().getHunts()) {
                if (hunt.hasTimedOut()) {
                    hunt.end();
                }
            }
        });
    }
}
