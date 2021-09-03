package com.envyful.pixel.hunt.remastered.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.forge.commands.PixelHuntCommand;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.hunt.PixelHuntForgeFactory;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonCaptureListener;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonSpawnListener;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.io.IOException;

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

    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private final ForgePlayerManager playerManager = new ForgePlayerManager();

    private PixelHuntConfig config;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        instance = this;

        this.loadConfig();

        GuiFactory.setPlatformFactory(new ForgeGuiFactory());
        PixelHuntFactory.setPlatformFactory(new PixelHuntForgeFactory(this));

        new ParticleDisplayTask();
        new PokemonSpawnListener(this);
        new PokemonCaptureListener(this);
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(PixelHuntConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        this.commandFactory.registerCommand(event.getServer(), new PixelHuntCommand());
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

    public ForgePlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
