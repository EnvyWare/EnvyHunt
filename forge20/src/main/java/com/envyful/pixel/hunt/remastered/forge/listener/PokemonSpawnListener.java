package com.envyful.pixel.hunt.remastered.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import com.mojang.serialization.Codec;
import com.pixelmonmod.pixelmon.client.render.shader.PixelmonShaders;
import com.pixelmonmod.pixelmon.client.render.shader.ShaderParameters;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.List;

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

                if (hunt.matchesDisplay(pixelmon)) {
                    if (hunt.shouldPlayParticles()) {
                        ParticleDisplayTask.addPokemon(pixelmon);
                    }

                    if (hunt.isCustomColour() && hunt.getColor() != null) {
                        var colour = hunt.getColor().getComponents(null);

                        pixelmon.setShaderParameters(ShaderParameters.builder()
                                .id(PixelmonShaders.FRESNEL_SHADER)
                                .noTexture()
                                .modelAndShader()
                                .withParameter("FresnelBias", Codec.FLOAT, 0.35F)
                                .withParameter("FresnelScale", Codec.FLOAT, 20F)
                                .withParameter("FresnelPower", Codec.FLOAT, 0.85F)
                                .withParameter("FresnelColor", Codec.FLOAT.listOf(), List.of(colour[0], colour[1], colour[2], colour[3]))
                                .renderColor(new Color(1F, 1F, 1F, 0.1F))
                                .build());
                    }
                }
            }
        });
    }
}
