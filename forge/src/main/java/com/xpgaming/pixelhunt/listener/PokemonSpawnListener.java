package com.xpgaming.pixelhunt.listener;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.xpgaming.pixelhunt.PixelHuntForge;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.hunt.PixelHuntFactory;
import com.xpgaming.pixelhunt.utils.UtilConcurrency;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PokemonSpawnListener {

    private final PixelHuntForge main;

    public PokemonSpawnListener(PixelHuntForge main) {
        this.main = main;
    }

    @SubscribeEvent
    public void onPokemonSpawn(EntityJoinWorldEvent event) {
        UtilConcurrency.runAsync(() -> {
            Entity entity = event.getEntity();

            if(!(entity instanceof EntityPixelmon)) {
                return;
            }

            EntityPixelmon pixelmon = (EntityPixelmon) entity;

            for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
                if (hunt.isBeingHunted(pixelmon.getPokemonData())) {
                    this.main.getDisplayTask().addPokemon(pixelmon);
                    return;
                }
            }
        });
    }
}
