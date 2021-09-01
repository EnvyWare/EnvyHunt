package com.xpgaming.pixelhunt.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiVideoSettings;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ConfigPath("config/PixelHuntRemastered/config.yml")
@ConfigSerializable
public class PixelHuntConfig {

    private ConfigInterface configInterface = new ConfigInterface();

    private List<String> spawnBroadcast = Lists.newArrayList();
    private List<String> timeoutBroadcast = Lists.newArrayList();

    public List<String> getSpawnBroadcast() {
        return this.spawnBroadcast;
    }

    public List<String> getTimeoutBroadcast() {
        return this.timeoutBroadcast;
    }
}
