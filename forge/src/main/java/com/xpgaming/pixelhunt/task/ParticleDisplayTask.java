package com.xpgaming.pixelhunt.task;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.hunt.PixelHuntFactory;
import com.xpgaming.pixelhunt.utils.UtilConcurrency;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.List;

public class ParticleDisplayTask {

    private final List<EntityPixelmon> huntPokemon = Lists.newCopyOnWriteArrayList();

    private int currentTick = 0;

    public void addPokemon(EntityPixelmon pixelmon) {
        this.huntPokemon.add(pixelmon);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        ++this.currentTick;

        if (this.currentTick % 10 != 0) {
            return;
        }

        UtilConcurrency.runAsync(() -> {
            Iterator<EntityPixelmon> iterator = this.huntPokemon.iterator();

            while (iterator.hasNext()) {
                EntityPixelmon pixelmon = iterator.next();

                if (pixelmon == null || pixelmon.isDead || pixelmon.hasOwner() || !this.isHuntPokemon(pixelmon.getPokemonData())) {
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
            if (hunt.isBeingHunted(pokemon)) {
                return true;
            }
        }

        return false;
    }

}
