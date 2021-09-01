package com.xpgaming.pixelhunt.config;

import com.google.common.collect.Lists;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ConfigSerializable
public class PixelHuntConfig {

    public static final Path CONFIG_PATH = Paths.get("config" + File.separator + "PixelHunt" + File.separator + "config.yml");

    private static ConfigurationNode configNode;

    public static PixelHuntConfig getInstance(Path path) {
        try {
            ObjectMapper<PixelHuntConfig> objectMapper = ObjectMapper.factory().get(PixelHuntConfig.class);
            YamlConfigurationLoader build = YamlConfigurationLoader.builder().path(path).build();

            configNode = build.load();
            return objectMapper.load(configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ConfigurationNode getConfigNode() {
        return configNode;
    }

    private int guiHeight = 3;
    private String guiName = "PixelHunt";
    private String backgroundItem = "minecraft:stained_glass_pane";
    private int backgroundItemDamage = 8;
    private boolean checkeredBackground = false;
    private String offColourBackgroundItem = "minecraft:stained_glass_pane";
    private int offColourBackgroundItemDamage = 9;
    private List<String> spawnBroadcast = Lists.newArrayList();
    private List<String> timeoutBroadcast = Lists.newArrayList();

    public int getGuiHeight() {
        return this.guiHeight;
    }

    public String getGuiName() {
        return this.guiName;
    }

    public String getBackgroundItem() {
        return this.backgroundItem;
    }

    public int getBackgroundItemDamage() {
        return this.backgroundItemDamage;
    }

    public List<String> getSpawnBroadcast() {
        return this.spawnBroadcast;
    }

    public boolean isCheckeredBackground() {
        return this.checkeredBackground;
    }

    public String getOffColourBackgroundItem() {
        return this.offColourBackgroundItem;
    }

    public int getOffColourBackgroundItemDamage() {
        return this.offColourBackgroundItemDamage;
    }

    public List<String> getTimeoutBroadcast() {
        return this.timeoutBroadcast;
    }
}
