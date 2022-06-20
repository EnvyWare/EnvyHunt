package com.envyful.pixel.hunt.remastered.forge.task;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
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
        if (!PixelHuntForge.getInstance().getConfig().isEnableParticles()) {
            return;
        }

        ++this.currentTick;

        if (this.currentTick % 10 != 0) {
            return;
        }

        Iterator<PixelmonEntity> iterator = this.huntPokemon.iterator();

        while (iterator.hasNext()) {
            PixelmonEntity pixelmon = iterator.next();

            if (pixelmon == null || !pixelmon.isAlive() || pixelmon.hasOwner()
                    || !this.isHuntPokemon(pixelmon.getPokemon())) {
                iterator.remove();
                continue;
            }

            ServerWorld worldServer = (ServerWorld)pixelmon.level;
            Vector3d positionVector = pixelmon.position();

            worldServer.addParticle(ParticleTypes.FLAME,
                    positionVector.x, positionVector.y, positionVector.z,
                    5, 0, 0.05);
        }

        UtilConcurrency.runAsync(() -> {
            for (PixelHunt allHunt : PixelHuntFactory.getAllHunts()) {
                if (allHunt.hasTimedOut()) {
                    allHunt.end();
                    allHunt.generatePokemon();
                }
            }
        });
    }

    private boolean isHuntPokemon(Pokemon pokemon) {
        for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
            if (hunt.isSpeciesHunted(pokemon)) {
                return true;
            }
        }

        return false;
    }

}
