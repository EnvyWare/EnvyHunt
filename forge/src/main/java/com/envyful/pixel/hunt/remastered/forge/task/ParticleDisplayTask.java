package com.envyful.pixel.hunt.remastered.forge.task;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.List;

public class ParticleDisplayTask extends LazyListener {

    private static ParticleDisplayTask instance;

    private final List<EntityPixelmon> huntPokemon = Lists.newArrayList();

    private int currentTick = 0;

    public ParticleDisplayTask() {
        super();

        instance = this;
    }

    public static void addPokemon(EntityPixelmon pixelmon) {
        instance.huntPokemon.add(pixelmon);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        ++this.currentTick;

        if (this.currentTick % 10 != 0) {
            return;
        }

        Iterator<EntityPixelmon> iterator = this.huntPokemon.iterator();

        while (iterator.hasNext()) {
            EntityPixelmon pixelmon = iterator.next();

            if (pixelmon == null || pixelmon.isDead || pixelmon.hasOwner()
                    || !this.isHuntPokemon(pixelmon.getPokemonData())) {
                iterator.remove();
                continue;
            }

            WorldServer worldServer = (WorldServer) pixelmon.getEntityWorld();
            Vec3d positionVector = pixelmon.getPositionVector();

            worldServer.spawnParticle(EnumParticleTypes.FLAME,
                    positionVector.x,
                    positionVector.y,
                    positionVector.z,
                    5,
                    0, 0, 0, 0.05);
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
