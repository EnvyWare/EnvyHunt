package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ConfigRandomWeightedSet;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.type.Pair;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigPath("config/EnvyHunt/config.yml")
@ConfigSerializable
public class PixelHuntConfig extends AbstractYamlConfig {

    private Map<String, HuntConfig> hunts = ImmutableMap.of(
            "one", new HuntConfig()
    );

    private List<String> blockedSpecies = Lists.newArrayList(
            "articuno",
            "zygarde",
            "nidorina"
    );

    private boolean catchesCountForMultipleHunts = false;

    public PixelHuntConfig() {
        super();
    }

    public List<HuntConfig> getHunts() {
        return Lists.newArrayList(this.hunts.values());
    }

    public boolean isCatchesCountForMultipleHunts() {
        return this.catchesCountForMultipleHunts;
    }

    public List<String> getBlockedSpecies() {
        return this.blockedSpecies;
    }

    @ConfigSerializable
    public static class HuntConfig {

        private String id = "one";
        private boolean playParticles = false;
        private String particles = "flame";
        private transient ParticleOptions particleCache = ParticleTypes.FLAME;
        private boolean customColour = true;
        private String colour = "#c7f2cb";
        private transient Color colourCache = null;

        private int page = 1;

        private boolean requiresPermission = false;
        private String permission = "none";

        private List<String> startCommands = Lists.newArrayList();
        private List<String> timeoutCommands = Lists.newArrayList();

        private List<String> requirementSpecs = Lists.newArrayList(
                "random",
                "randomivpercent:10-20",
                "randomability:stickyhold,cursedbody,shadowshield",
                "randomgender",
                "randomgrowths:Ordinary,Huge,Giant:2",
                "randomnatures:hardy,serious,quirky,bashful:3"
        );
        private transient List<PokemonSpecification> requirementSpecCache = null;

        private ConfigRandomWeightedSet<Reward> rewardCommands = new ConfigRandomWeightedSet<>(
                new ConfigRandomWeightedSet.WeightedObject<>(10, new Reward(
                        "hello",
                        Lists.newArrayList("broadcast %reward%"),
                        Lists.newArrayList("hello %player%")))
        );
        private List<String> rewardSpecs = Lists.newArrayList("maxivs");
        private transient List<PokemonSpecification> rewardSpecsCache = null;

        private boolean persistent = false;
        private long maxDurationMinutes = 30;
        private transient long currentStart = System.currentTimeMillis();

        private ExtendedConfigItem displayItem = ExtendedConfigItem.builder()
                .name("This is the example")
                .amount(1)
                .type("minecraft:diamond")
                .positions(Pair.of(1, 1))
                .lore(
                        "Requirements: ",
                        " > %species% ",
                        " > %ivs%",
                        " > %ability%",
                        " > %gender%",
                        " > %growth_1%",
                        " > %growth_2%",
                        " > %nature_1%",
                        " > %nature_2%",
                        " > %nature_3%"
                )
                .build();

        private boolean allowRewardUI = true;
        private HuntRewardUI rewardUI = new HuntRewardUI();

        public HuntConfig() {}

        public Color getColor() {
            if (this.colourCache == null) {
                this.colourCache = this.parseColor(this.colour);
            }

            return this.colourCache;
        }

        public boolean isCustomColour() {
            return this.customColour;
        }

        private Color parseColor(String hex) {
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }

            try {
                int i = Integer.parseInt(hex, 16);
                return new Color(i);
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        }

        public int getPage() {
            return this.page;
        }

        public String getId() {
            return this.id;
        }

        public boolean shouldPlayParticles() {
            return this.playParticles;
        }

        public ParticleOptions getParticles() {
            if (this.particleCache == null) {
                this.particleCache = (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocationHelper.of(this.particles));
            }

            return this.particleCache;
        }

        public boolean matchesHunt(PixelmonEntity pixelmon) {
            for (PokemonSpecification requirementSpec : this.getRequirementSpecs()) {
                if (requirementSpec != null && !requirementSpec.matches(pixelmon)) {
                    return false;
                }
            }

            return true;
        }

        public boolean matchesHunt(Pokemon pokemon) {
            for (PokemonSpecification requirementSpec : this.getRequirementSpecs()) {
                if (requirementSpec != null && !requirementSpec.matches(pokemon)) {
                    return false;
                }
            }

            return true;
        }

        public List<PokemonSpecification> getRequirementSpecs() {
            if (this.requirementSpecCache == null) {
                this.requirementSpecCache = Lists.newArrayList();

                for (String requirementSpec : this.requirementSpecs) {
                    this.requirementSpecCache.add(PokemonSpecificationProxy.create(requirementSpec));
                }
            }

            return this.requirementSpecCache;
        }

        public void reset() {
            this.requirementSpecCache = null;
            this.currentStart = System.currentTimeMillis();
        }

        public void rewardHunt(ServerPlayer player, Pokemon pokemon) {
            if (!this.persistent) {
                this.reset();
            }

            Reward random = this.rewardCommands.getRandom();

            if (random != null) {
                random.execute(player, pokemon);
            }

            for (PokemonSpecification rewardSpec : this.getRewardSpecs()) {
                rewardSpec.apply(pokemon);
            }
        }

        private List<PokemonSpecification> getRewardSpecs() {
            if (this.rewardSpecs == null || this.rewardSpecs.isEmpty()) {
                return Collections.emptyList();
            }

            if (this.rewardSpecsCache == null) {
                this.rewardSpecsCache = Lists.newArrayListWithCapacity(this.rewardSpecs.size());
                for (String rewardSpec : this.rewardSpecs) {
                    this.rewardSpecsCache.add(PokemonSpecificationProxy.create(rewardSpec));
                }
            }
            return this.rewardSpecsCache;
        }

        public ExtendedConfigItem getDisplayItem() {
            return this.displayItem;
        }

        public boolean canParticipate(ServerPlayer player) {
            if (!this.requiresPermission) {
                return true;
            }

            return UtilPlayer.hasPermission(player, this.permission);
        }

        public boolean isAllowRewardUI() {
            return this.allowRewardUI;
        }

        public ConfigRandomWeightedSet<Reward> getRewardCommands() {
            return this.rewardCommands;
        }

        public HuntRewardUI getRewardUI() {
            return this.rewardUI;
        }

        public boolean hasTimedOut() {
            if (this.persistent) {
                return false;
            }

            return (System.currentTimeMillis() - this.currentStart) >= TimeUnit.MINUTES.toMillis(this.maxDurationMinutes);
        }

        public void end() {
            this.reset();

            for (String s : this.timeoutCommands) {
                UtilForgeServer.executeCommand(s);
            }

            for (String spawnCommand : this.startCommands) {
                UtilForgeServer.executeCommand(spawnCommand);
            }
        }

        public long getCurrentStart() {
            return this.currentStart;
        }

        public long getMaxDurationMinutes() {
            return this.maxDurationMinutes;
        }
    }

    @ConfigSerializable
    public static class Reward {

        private String name;
        private int page = 1;
        private List<String> commands;
        private List<String> messages;
        private ExtendedConfigItem displayItem = ExtendedConfigItem.builder()
                .type("minecraft:stone")
                .amount(1)
                .name("Rewrad")
                .lore("reward")
                .positions(Pair.of(1, 1))
                .build();

        public Reward(String name, List<String> commands, List<String> messages) {
            this.name = name;
            this.commands = commands;
            this.messages = messages;
        }
        public Reward() {
        }

        public int getPage() {
            return this.page;
        }

        public ExtendedConfigItem getDisplayItem() {
            return this.displayItem;
        }

        public void execute(ServerPlayer player, Pokemon pokemon) {
            if (this.commands != null && !this.commands.isEmpty()) {
                for (String command : this.commands) {
                    UtilForgeServer.executeCommand(command.replace("%player%", player.getName().getString())
                            .replace("%reward%", this.name)
                            .replace("%pokemon%", pokemon.getDisplayName()));
                }
            }

            if (this.messages != null && !this.messages.isEmpty()) {
                for (String message : this.messages) {
                    player.sendSystemMessage(UtilChatColour.colour(message.replace("%player%", player.getName().getString())
                            .replace("%reward%", this.name)
                            .replace("%pokemon%", pokemon.getDisplayName())));
                }
            }
        }
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
