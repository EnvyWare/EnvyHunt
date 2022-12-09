package com.envyful.pixel.hunt.remastered.forge;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.pixel.hunt.remastered.forge.commands.PixelHuntCommand;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntGraphics;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonCaptureListener;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonSpawnListener;
import com.envyful.pixel.hunt.remastered.forge.spec.RandomAbilityRequirement;
import com.envyful.pixel.hunt.remastered.forge.spec.RandomNaturesRequirement;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("envyhunt")
public class PixelHuntForge {

    private static PixelHuntForge instance;

    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory();
    private final ForgePlayerManager playerManager = new ForgePlayerManager();
    private final Logger logger = LogManager.getLogger("envyhunt");

    private PixelHuntConfig config;
    private PixelHuntGraphics graphics;

    public PixelHuntForge() {
        UtilLogger.setLogger(this.logger);
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;

        PokemonSpecificationProxy.register(new RandomAbilityRequirement());
        PokemonSpecificationProxy.register(new RandomNaturesRequirement());
    }

    @SubscribeEvent
    public void onPreInit(FMLServerAboutToStartEvent event) {
        instance = this;

        this.loadConfig();

        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        new ParticleDisplayTask();
        new PokemonSpawnListener();
        new PokemonCaptureListener();
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(PixelHuntConfig.class);
            this.graphics = YamlConfigFactory.getInstance(PixelHuntGraphics.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), new PixelHuntCommand());
    }

    public static PixelHuntForge getInstance() {
        return instance;
    }

    public static PixelHuntConfig getConfig() {
        return instance.config;
    }

    public static PixelHuntGraphics getGraphics() {
        return instance.graphics;
    }

    public static ForgePlayerManager getPlayerManager() {
        return instance.playerManager;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

}
