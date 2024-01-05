package com.envyful.pixel.hunt.remastered.forge;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.command.parser.ForgeAnnotationCommandParser;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.pixel.hunt.remastered.forge.commands.PixelHuntCommand;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntGraphics;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonCaptureListener;
import com.envyful.pixel.hunt.remastered.forge.listener.PokemonSpawnListener;
import com.envyful.pixel.hunt.remastered.forge.spec.*;
import com.envyful.pixel.hunt.remastered.forge.task.ParticleDisplayTask;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod("envyhunt")
public class EnvyHunt {

    private static EnvyHunt instance;

    private final ForgePlayerManager playerManager = new ForgePlayerManager();
    private final ForgeCommandFactory commandFactory = new ForgeCommandFactory(ForgeAnnotationCommandParser::new, playerManager);
    private final Logger logger = LogManager.getLogger("envyhunt");

    private PixelHuntConfig config;
    private PixelHuntGraphics graphics;

    public EnvyHunt() {
        UtilLogger.setLogger(this.logger);
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;

        PokemonSpecificationProxy.register(new RandomAbilityRequirement());
        PokemonSpecificationProxy.register(new RandomNaturesRequirement());
        PokemonSpecificationProxy.register(new RandomGenderRequirement());
        PokemonSpecificationProxy.register(new RandomIVPercentageRequirement());
        PokemonSpecificationProxy.register(new RandomGrowthsRequirement());
        PokemonSpecificationProxy.register(new RandomSpeciesIngoringBlockedRequirement());
        PokemonSpecificationProxy.register(new IVPercentageRangeRequirement());

        this.loadConfig();
    }

    @SubscribeEvent
    public void onPreInit(ServerAboutToStartEvent event) {
        instance = this;

        GuiFactory.setPlatformFactory(new ForgeGuiFactory());

        PokemonSpecificationProxy.register(new MaxIVRequirement());

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
        this.commandFactory.registerCommand(event.getDispatcher(), this.commandFactory.parseCommand(new PixelHuntCommand()));
    }

    public static EnvyHunt getInstance() {
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
