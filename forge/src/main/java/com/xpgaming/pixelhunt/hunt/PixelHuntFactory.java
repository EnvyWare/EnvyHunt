package com.xpgaming.pixelhunt.hunt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xpgaming.pixelhunt.config.PixelHuntConfig;
import com.xpgaming.pixelhunt.hunt.impl.SimplePixelHunt;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PixelHuntFactory {

    private static final Map<String, PixelHunt> LOADED_HUNTS = Maps.newHashMap();

    public static void init(ConfigurationNode config) {
        LOADED_HUNTS.clear();

        for (Map.Entry<Object, ? extends ConfigurationNode> hunts : config.node("hunts").childrenMap().entrySet()) {
            PixelHunt hunt = new SimplePixelHunt(hunts.getKey().toString(), hunts.getValue());

            LOADED_HUNTS.put(hunt.getIdentifier().toLowerCase(), hunt);
            hunt.generatePokemon();
        }
    }

    public static void reloadAll() {
        getAllHunts().forEach(PixelHunt::end);
        LOADED_HUNTS.clear();
        init(PixelHuntConfig.getConfigNode());
    }

    public static List<PixelHunt> getAllHunts() {
        return Collections.unmodifiableList(Lists.newArrayList(LOADED_HUNTS.values()));
    }

    public static PixelHunt getHunt(String identifier) {
        return LOADED_HUNTS.get(identifier.toLowerCase());
    }
}
