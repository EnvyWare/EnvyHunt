package com.envyful.pixel.hunt.remastered.forge.config;

import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.neoforge.config.ConfigReward;
import com.envyful.api.neoforge.config.ConfigRewardPool;
import com.envyful.api.neoforge.player.util.UtilPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.type.Pair;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.google.common.collect.Lists;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigSerializable
public class HuntConfig extends AbstractYamlConfig {

    private boolean enabled = true;
    private String id;
    private boolean playParticles;
    private String particles;
    private transient ParticleOptions particleCache;
    private boolean customColour;
    private String colour;
    private transient Color colourCache = null;

    private int page;
    private boolean requiresPermission;
    private String permission;

    private List<String> startCommands;
    private List<String> timeoutCommands;

    private List<String> requirementSpecs;
    private transient List<PokemonSpecification> requirementSpecCache = null;
    private List<String> particleSpecs;
    private transient List<PokemonSpecification> particleSpecCache = null;

    private ConfigRewardPool<ConfigReward> rewards;
    private List<String> rewardSpecs;
    private transient List<PokemonSpecification> rewardSpecsCache = null;

    private boolean persistent;
    private long maxDurationMinutes;
    private transient long currentStart = System.currentTimeMillis();

    private ExtendedConfigItem displayItem;

    private boolean allowRewardUI;
    private PixelHuntConfig.HuntRewardUI rewardUI;
    private Map<String, ExtendedConfigItem> rewardDisplayItems;

    public HuntConfig(Builder builder) {
        super();

        this.id = builder.id;
        this.playParticles = builder.playParticles;
        this.particles = builder.particles;
        this.customColour = builder.customColour;
        this.colour = builder.colour;
        this.page = builder.page;
        this.requiresPermission = builder.requiresPermission;
        this.permission = builder.permission;
        this.startCommands = builder.startCommands;
        this.timeoutCommands = builder.timeoutCommands;
        this.requirementSpecs = builder.requirementSpecs;
        this.rewards = builder.rewards;
        this.rewardSpecs = builder.rewardSpecs;
        this.persistent = builder.persistent;
        this.maxDurationMinutes = builder.maxDurationMinutes;
        this.displayItem = builder.displayItem;
        this.allowRewardUI = builder.allowRewardUI;
        this.rewardUI = builder.rewardUI;
        this.rewardDisplayItems = builder.rewardDisplayItems;
        this.particleSpecs = builder.requirementSpecs;
    }

    public HuntConfig() {
        super();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

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
            var particleRegistry = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registries.PARTICLE_TYPE);
//            this.particleCache = particleRegistry.get(ResourceLocationHelper.of(this.particles));
        }

        return this.particleCache;
    }

    public boolean matchesHunt(PixelmonEntity pixelmon) {
        if (!this.isEnabled()) {
            return false;
        }

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

    public boolean matchesDisplay(PixelmonEntity pixelmon) {
        if (!this.isEnabled()) {
            return false;
        }

        for (PokemonSpecification requirementSpec : this.getParticleSpecs()) {
            if (requirementSpec != null && !requirementSpec.matches(pixelmon)) {
                return false;
            }
        }

        return true;
    }

    public boolean matchesDisplay(Pokemon pokemon) {
        for (PokemonSpecification requirementSpec : this.getParticleSpecs()) {
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
                var parseAttempt = PokemonSpecificationProxy.create(requirementSpec);

                if (!parseAttempt.wasSuccess()) {
                    EnvyHunt.getLogger().error("Failed to parse spec in hunt " + this.id + " for spec " + requirementSpec + " for reason: " + parseAttempt.getError());
                    continue;
                }

                this.requirementSpecCache.add(parseAttempt.get());
            }
        }

        return this.requirementSpecCache;
    }

    public List<PokemonSpecification> getParticleSpecs() {
        if (this.particleSpecs == null || this.particleSpecs.isEmpty()) {
            return this.getRequirementSpecs();
        }

        if (this.particleSpecCache == null) {
            this.particleSpecCache = Lists.newArrayList();

            for (String particleSpec : this.particleSpecs) {
                this.particleSpecCache.add(PokemonSpecificationProxy.create(particleSpec).get());
            }
        }

        return this.particleSpecCache;
    }

    public void reset() {
        this.requirementSpecCache = null;
        this.currentStart = System.currentTimeMillis();
    }

    public void rewardHunt(ServerPlayer player, Pokemon pokemon) {
        if (!this.persistent) {
            this.reset();

            PlatformProxy.executeConsoleCommands(this.startCommands);
        }

        this.rewards.give(player);

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
                this.rewardSpecsCache.add(PokemonSpecificationProxy.create(rewardSpec).get());
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

    public PixelHuntConfig.HuntRewardUI getRewardUI() {
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

        PlatformProxy.executeConsoleCommands(this.timeoutCommands);
        PlatformProxy.executeConsoleCommands(this.startCommands);
    }

    public long getCurrentStart() {
        return this.currentStart;
    }

    public long getMaxDurationMinutes() {
        return this.maxDurationMinutes;
    }

    public List<ExtendedConfigItem> getRewardDisplayItems() {
        return Lists.newArrayList(this.rewardDisplayItems.values());
    }

    public static Builder builder(String id) {
        return new Builder().id(id);
    }

    public static class Builder {

        private String id;
        private boolean playParticles = false;
        private String particles = "flame";
        private boolean customColour = true;
        private String colour = "#c7f2cb";
        private int page;
        private boolean requiresPermission = false;
        private String permission;
        private List<String> startCommands = Lists.newArrayList();
        private List<String> timeoutCommands = Lists.newArrayList();
        private List<String> requirementSpecs = Lists.newArrayList();
        private ConfigRewardPool<ConfigReward> rewards;
        private List<String> rewardSpecs = Lists.newArrayList();
        private boolean persistent = false;
        private long maxDurationMinutes = 30;

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
        private boolean allowRewardUI = false;
        private PixelHuntConfig.HuntRewardUI rewardUI;
        private Map<String, ExtendedConfigItem> rewardDisplayItems = new HashMap<>();

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder playParticles(String particles) {
            this.playParticles = true;
            this.particles = particles;
            return this;
        }

        public Builder customColour(String colour) {
            this.customColour = true;
            this.colour = colour;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder requiresPermission(String permission) {
            this.requiresPermission = true;
            this.permission = permission;
            return this;
        }

        public Builder startCommands(List<String> startCommands) {
            this.startCommands.addAll(startCommands);
            return this;
        }

        public Builder timeoutCommands(List<String> timeoutCommands) {
            this.timeoutCommands.addAll(timeoutCommands);
            return this;
        }

        public Builder requirementSpecs(List<String> requirementSpecs) {
            this.requirementSpecs.addAll(requirementSpecs);
            return this;
        }

        public Builder rewards(ConfigRewardPool<ConfigReward> rewards) {
            this.rewards = rewards;
            return this;
        }

        public Builder rewardSpecs(List<String> rewardSpecs) {
            this.rewardSpecs.addAll(rewardSpecs);
            return this;
        }

        public Builder persistent() {
            this.persistent = true;
            return this;
        }

        public Builder maxDurationMinutes(long maxDurationMinutes) {
            this.maxDurationMinutes = maxDurationMinutes;
            return this;
        }

        public Builder displayItem(ExtendedConfigItem displayItem) {
            this.displayItem = displayItem;
            return this;
        }

        public Builder rewardUI(PixelHuntConfig.HuntRewardUI rewardUI) {
            this.allowRewardUI = true;
            this.rewardUI = rewardUI;
            return this;
        }

        public Builder rewardDisplayItems(ExtendedConfigItem... rewardDisplayItems) {
            for (ExtendedConfigItem rewardDisplayItem : rewardDisplayItems) {
                this.rewardDisplayItems.put(String.valueOf(this.rewardDisplayItems.size() + 1), rewardDisplayItem);
            }

            return this;
        }

        public HuntConfig build() {
            return new HuntConfig(this);
        }
    }
}
