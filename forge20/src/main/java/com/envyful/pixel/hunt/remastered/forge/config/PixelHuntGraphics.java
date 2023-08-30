
package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.type.Pair;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigPath("config/EnvyHunt/guis.yml")
@ConfigSerializable
public class PixelHuntGraphics extends AbstractYamlConfig {

    private HuntUI huntUI = new HuntUI();

    public PixelHuntGraphics() {
        super();
    }

    public HuntUI getHuntUI() {
        return this.huntUI;
    }

    @ConfigSerializable
    public static class HuntUI {

        private ConfigInterface guiSettings = new ConfigInterface(
                "Example", 3, ConfigInterface.FillType.BLOCK.name(), ImmutableMap.of(
                "one", ConfigItem.builder()
                        .type("minecraft:black_stained_glass_pane")
                        .amount(1)
                        .name(" ")
                        .build())
        );

        private ExtendedConfigItem nextPage = ExtendedConfigItem.builder()
                .name("Next page")
                .amount(1)
                .type("pixelmon:right_trade_holder")
                .positions(Pair.of(5, 0))
                .lore("NEXT PAGE!")
                .build();


        private ExtendedConfigItem previousPage = ExtendedConfigItem.builder()
                .name("Previous page")
                .amount(1)
                .type("pixelmon:left_trade_holder")
                .positions(Pair.of(4, 0))
                .lore("PREVIOUS PAGE!")
                .build();

        private int pages = 1;

        public ConfigInterface getGuiSettings() {
            return this.guiSettings;
        }

        public ExtendedConfigItem getNextPage() {
            return this.nextPage;
        }

        public ExtendedConfigItem getPreviousPage() {
            return this.previousPage;
        }

        public int getPages() {
            return this.pages;
        }
    }
}
