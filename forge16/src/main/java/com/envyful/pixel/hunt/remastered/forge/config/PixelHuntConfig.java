package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.data.Serializers;
import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ConfigRandomWeightedSet;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.type.Pair;
import com.envyful.pixel.hunt.remastered.forge.config.typeadapter.ParticleDataTypeAdapter;
import com.envyful.pixel.hunt.remastered.forge.config.typeadapter.PokemonSpecificationTypeAdapter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Util;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigPath("config/EnvyHunt/config.yml")
@ConfigSerializable
@Serializers({ParticleDataTypeAdapter.class, PokemonSpecificationTypeAdapter.class})
public class PixelHuntConfig extends AbstractYamlConfig {

    private Map<String, HuntConfig> hunts = ImmutableMap.of(
            "one", new HuntConfig()
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

    @ConfigSerializable
    public static class HuntConfig {

        private String id = "one";
        private boolean playParticles = false;
        private IParticleData particles = ParticleTypes.FLAME;
        private boolean customColour = true;
        private String colour = "#c7f2cb";
        private transient Color colourCache = null;

        private int page = 1;

        private boolean requiresPermission = false;
        private String permission = "none";

        private List<String> startCommands = Lists.newArrayList();
        private List<String> timeoutCommands = Lists.newArrayList();

        private List<PokemonSpecification> requirementSpecs = Lists.newArrayList(PokemonSpecificationProxy.create("shiny"));

        private ConfigRandomWeightedSet<Reward> rewardCommands = new ConfigRandomWeightedSet<>(
                new ConfigRandomWeightedSet.WeightedObject<>(10, new Reward(
                        "hello",
                        Lists.newArrayList("broadcast %reward%"),
                        Lists.newArrayList("hello %player%")))
        );
        private List<PokemonSpecification> rewardSpecs = Lists.newArrayList(PokemonSpecificationProxy.create("maxivs"));

        private boolean persistent = false;
        private long maxDurationMinutes = 30;
        private long currentStart = System.currentTimeMillis();

        private ExtendedConfigItem displayItem = ExtendedConfigItem.builder()
                .name("This is the example")
                .amount(1)
                .type("minecraft:diamond")
                .positions(Pair.of(1, 1))
                .lore(
                        "Requirements: ",
                        " > Shiny"
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

        public boolean shouldTimeOut() {
            return !this.persistent;
        }

        public IParticleData getParticles() {
            return this.particles;
        }

        public boolean matchesHunt(PixelmonEntity pixelmon) {
            for (PokemonSpecification requirementSpec : this.requirementSpecs) {
                if (requirementSpec != null && !requirementSpec.matches(pixelmon)) {
                    return false;
                }
            }

            return true;
        }

        public boolean matchesHunt(Pokemon pokemon) {
            for (PokemonSpecification requirementSpec : this.requirementSpecs) {
                if (requirementSpec != null && !requirementSpec.matches(pokemon)) {
                    return false;
                }
            }

            return true;
        }

        public void rewardHunt(ServerPlayerEntity player, Pokemon pokemon) {
            Reward random = this.rewardCommands.getRandom();

            if (random != null) {
                random.execute(player, pokemon);
            }

            if (this.rewardSpecs != null) {
                for (PokemonSpecification rewardSpec : this.rewardSpecs) {
                    rewardSpec.apply(pokemon);
                }
            }
        }

        public ExtendedConfigItem getDisplayItem() {
            return this.displayItem;
        }

        public boolean canParticipate(ServerPlayerEntity player) {
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
            return (System.currentTimeMillis() - this.currentStart) >= TimeUnit.MINUTES.toMillis(this.maxDurationMinutes);
        }

        public void end() {
            List<PokemonSpecification> specs = Lists.newArrayList();

            for (PokemonSpecification requirementSpecs : this.requirementSpecs) {
                specs.add(PokemonSpecificationProxy.create(requirementSpecs.toString()));
            }

            this.requirementSpecs = specs;

            for (String s : this.timeoutCommands) {
                UtilForgeServer.executeCommand(s);
            }

            for (String spawnCommand : this.startCommands) {
                UtilForgeServer.executeCommand(spawnCommand);
            }
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

        public void execute(ServerPlayerEntity player, Pokemon pokemon) {
            if (this.commands != null && !this.commands.isEmpty()) {
                for (String command : this.commands) {
                    UtilForgeServer.executeCommand(command.replace("%player%", player.getName().getString())
                            .replace("%reward%", this.name)
                            .replace("%pokemon%", pokemon.getDisplayName()));
                }
            }

            if (this.messages != null && !this.messages.isEmpty()) {
                for (String message : this.messages) {
                    player.sendMessage(UtilChatColour.colour(message.replace("%player%", player.getName().getString())
                            .replace("%reward%", this.name)
                            .replace("%pokemon%", pokemon.getDisplayName())), Util.NIL_UUID);
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
