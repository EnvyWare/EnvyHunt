package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.hunt.PixelHuntForgeFactory;
import com.envyful.pixel.hunt.remastered.forge.utils.UtilConcurrency;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
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

            for (PixelHunt hunt : PixelHuntForgeFactory.getAllHunts()) {
                if (hunt.isBeingHunted(pixelmon.getPokemonData())) {
                    this.main.getDisplayTask().addPokemon(pixelmon);
                    return;
                }
            }
        });
    }
}
