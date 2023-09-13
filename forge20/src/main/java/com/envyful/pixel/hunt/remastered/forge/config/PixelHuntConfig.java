package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ConfigRandomWeightedSet;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.config.ConfigReward;
import com.envyful.api.forge.config.ConfigRewardPool;
import com.envyful.api.type.Pair;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.List;

@ConfigPath("config/EnvyHunt/config.yml")
@ConfigSerializable
public class PixelHuntConfig extends AbstractYamlConfig {

    private transient List<HuntConfig> hunts = Lists.newArrayList();

    private List<String> blockedSpecies = Lists.newArrayList(
            "articuno",
            "zygarde",
            "nidorina"
    );

    private boolean catchesCountForMultipleHunts = false;

    public PixelHuntConfig() throws IOException {
        super();

        this.hunts.addAll(YamlConfigFactory.getInstances(HuntConfig.class, "config/EnvyHunt/hunts/",
                DefaultConfig.onlyNew("defaults/basic.yml", HuntConfig.builder("example")
                        .playParticles("flame")
                        .customColour("#c7f2cb")
                        .page(1)
                        .requiresPermission("envy.hunt.example")
                        .startCommands(Lists.newArrayList("broadcast Example hunt is now begining!"))
                        .timeoutCommands(Lists.newArrayList("broadcast Example hunt ran out of time!"))
                        .requirementSpecs(Lists.newArrayList(
                                "randomnonblockedspecies",
                                "randomivpercent:10-20",
                                "randomability:stickyhold,cursedbody,shadowshield",
                                "randomgender",
                                "randomgrowths:Ordinary,Huge,Giant:2",
                                "randomnatures:hardy,serious,quirky,bashful:3"
                        ))
                        .rewards(ConfigRewardPool.builder()
                                .guranteedReward(new ConfigReward(Lists.newArrayList("broadcast Example hunt was completed by %player%"), Lists.newArrayList()))
                                .maxRolls(1)
                                .minRolls(1)
                                .rewards(new ConfigRandomWeightedSet<>(
                                        new ConfigRandomWeightedSet.WeightedObject<>(10, new ConfigReward(Lists.newArrayList("broadcast Example hunt was completed by %player%"), Lists.newArrayList())),
                                        new ConfigRandomWeightedSet.WeightedObject<>(1, new ConfigReward(Lists.newArrayList("broadcast Example hunt was completed by %player%"), Lists.newArrayList()))
                                ))
                                .build())
                        .rewardSpecs(Lists.newArrayList("maxivs"))
                        .maxDurationMinutes(30)
                        .displayItem(ExtendedConfigItem.builder()
                                .name("This is the example")
                                .amount(1)
                                .type("pixelmon:pixelmon_sprite")
                                .positions(Pair.of(1, 1))
                                .lore(
                                        "&fRequirements: ",
                                        "&a > %species% ",
                                        "&a > %ivs%",
                                        "&a > %ability%",
                                        "&a > %gender%",
                                        "&a > %growth_1%",
                                        "&a > %growth_2%",
                                        "&a > %nature_1%",
                                        "&a > %nature_2%",
                                        "&a > %nature_3%"
                                )
                                .nbt("ndex", new ConfigItem.NBTValue("short", "%dex_number%"))
                                .nbt("form", new ConfigItem.NBTValue("string", ""))
                                .nbt("gender", new ConfigItem.NBTValue("byte", "0"))
                                .nbt("palette", new ConfigItem.NBTValue("string", "none"))
                                .build())
                        .rewardUI(new HuntRewardUI())
                        .rewardDisplayItems(ExtendedConfigItem.builder()
                                .name("Example reward")
                                .amount(1)
                                .type("minecraft:diamond")
                                .positions(Pair.of(1, 1))
                                .build())
                        .build()),
                DefaultConfig.onlyNew("defaults/shinies.yml", HuntConfig.builder("shinies")
                        .playParticles("flame")
                        .customColour("#c7f2cb")
                        .page(1)
                        .requiresPermission("envy.hunt.example.shinies")
                        .startCommands(Lists.newArrayList("broadcast Shiny hunt is now begining!"))
                        .timeoutCommands(Lists.newArrayList("broadcast Shiny hunt ran out of time!"))
                        .requirementSpecs(Lists.newArrayList(
                                "randomnonblockedspecies",
                                "randomivpercent:10-20",
                                "randomability:stickyhold,cursedbody,shadowshield",
                                "randomgender",
                                "randomgrowths:Ordinary,Huge,Giant:2",
                                "randomnatures:hardy,serious,quirky,bashful:3",
                                "shiny"
                        ))
                        .rewards(ConfigRewardPool.builder()
                                .guranteedReward(new ConfigReward(Lists.newArrayList("broadcast Shiny hunt was completed by %player%"), Lists.newArrayList()))
                                .maxRolls(1)
                                .minRolls(1)
                                .rewards(new ConfigRandomWeightedSet<>(
                                        new ConfigRandomWeightedSet.WeightedObject<>(10, new ConfigReward(Lists.newArrayList("broadcast Shiny hunt was completed by %player%"), Lists.newArrayList())),
                                        new ConfigRandomWeightedSet.WeightedObject<>(1, new ConfigReward(Lists.newArrayList("broadcast Shiny hunt was completed by %player%"), Lists.newArrayList()))
                                ))
                                .build())
                        .rewardSpecs(Lists.newArrayList("maxivs"))
                        .maxDurationMinutes(30)
                        .displayItem(ExtendedConfigItem.builder()
                                .name("This is the example")
                                .amount(1)
                                .type("pixelmon:pixelmon_sprite")
                                .positions(Pair.of(3, 1))
                                .lore(
                                        "&fRequirements: ",
                                        "&a > %species% ",
                                        "&a > %ivs%",
                                        "&a > %ability%",
                                        "&a > %gender%",
                                        "&a > %growth_1%",
                                        "&a > %growth_2%",
                                        "&a > %nature_1%",
                                        "&a > %nature_2%",
                                        "&a > %nature_3%"
                                )
                                .nbt("ndex", new ConfigItem.NBTValue("short", "%dex_number%"))
                                .nbt("form", new ConfigItem.NBTValue("string", ""))
                                .nbt("gender", new ConfigItem.NBTValue("byte", "0"))
                                .nbt("palette", new ConfigItem.NBTValue("string", "shiny"))
                                .build())
                        .rewardUI(new HuntRewardUI())
                        .rewardDisplayItems(ExtendedConfigItem.builder()
                                .name("Example reward")
                                .amount(1)
                                .type("minecraft:diamond")
                                .positions(Pair.of(1, 1))
                                .build())
                        .build())
        ));
    }

    public List<HuntConfig> getHunts() {
        return this.hunts;
    }

    public boolean isCatchesCountForMultipleHunts() {
        return this.catchesCountForMultipleHunts;
    }

    public List<String> getBlockedSpecies() {
        return this.blockedSpecies;
    }

    @ConfigSerializable
    public static class HuntRewardUI {

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
