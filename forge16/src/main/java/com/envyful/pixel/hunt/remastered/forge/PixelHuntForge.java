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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.io.IOException;

@Mod("pixelhuntremastered")
public class PixelHuntForge {

    private static PixelHuntForge instance;

    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private final ForgePlayerManager playerManager = new ForgePlayerManager();

    private PixelHuntConfig config;

    public PixelHuntForge() {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    @SubscribeEvent
    public void onPreInit(FMLServerAboutToStartEvent event) {
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

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), new PixelHuntCommand());
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
