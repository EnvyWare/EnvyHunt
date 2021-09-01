package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

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
