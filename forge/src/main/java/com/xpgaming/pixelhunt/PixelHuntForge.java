package com.xpgaming.pixelhunt;

import com.envyful.api.forge.command.ForgeCommandFactory;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.xpgaming.pixelhunt.commands.PixelHuntCommand;
import com.xpgaming.pixelhunt.config.PixelHuntConfig;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.hunt.PixelHuntFactory;
import com.xpgaming.pixelhunt.listener.PokemonCaptureListener;
import com.xpgaming.pixelhunt.listener.PokemonSpawnListener;
import com.xpgaming.pixelhunt.task.ParticleDisplayTask;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Mod(
        modid = "pixelhuntremastered",
        name = "PixelHuntRemastered Forge",
        version = PixelHuntForge.MOD_VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "required-after:pixelmon;"
)
public class PixelHuntForge {

    public static final String MOD_VERSION = "0.1.0";

    private static PixelHuntForge instance;

    private final ParticleDisplayTask displayTask = new ParticleDisplayTask();
    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private PixelHuntConfig config;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        instance = this;
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    @Mod.EventHandler
    public void onServerShutdown(FMLServerStoppingEvent event) {

    }

    public static final PixelHuntForge getInstance() {
        return instance;
    }

    public PixelHuntConfig getConfig() {
        return this.config;
    }
}
