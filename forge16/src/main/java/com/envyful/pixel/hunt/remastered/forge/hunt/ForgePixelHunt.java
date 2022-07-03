package com.envyful.pixel.hunt.remastered.forge.hunt;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.math.UtilRandom;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.reforged.pixelmon.PokemonGenerator;
import com.envyful.api.reforged.pixelmon.PokemonSpec;
import com.envyful.api.time.UtilTimeFormat;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.event.PixelHuntStartEvent;
import com.envyful.pixel.hunt.remastered.forge.event.PixelHuntWonEvent;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ForgePixelHunt implements PixelHunt {

    private final List<String> rewardCommands = Lists.newArrayList();
    private final List<String> rewardDescription = Lists.newArrayList();

    private final PixelHuntConfig.HuntConfig huntConfig;
    private PokemonGenerator generator;
    private ItemStack displayItem;
    private PokemonSpec currentPokemon;
    private boolean randomCommands;
    private boolean maxIvs;
    private boolean ivMultiplierEnabled;
    private float ivMultiplier;
    private long duration;
    private long currentStart;
    private int guiX;
    private int guiY;

    public ForgePixelHunt(PixelHuntConfig.HuntConfig huntConfig) {
        this.huntConfig = huntConfig;
        this.generator = new PokemonGenerator(huntConfig.getGeneratorConfig());
        this.randomCommands = huntConfig.isRandomCommands();
        this.maxIvs = huntConfig.isMaxIvs();
        this.ivMultiplierEnabled = huntConfig.isIvMultiplierEnabled();
        this.ivMultiplier = huntConfig.getIvMultiplier();
        this.duration = TimeUnit.MINUTES.toMillis(huntConfig.getMaxDurationMinutes());
        this.rewardCommands.addAll(huntConfig.getRewardCommands());
        this.rewardDescription.addAll(huntConfig.getRewardDescription());
        this.guiX = huntConfig.getGuiX();
        this.guiY = huntConfig.getGuiY();
    }

    @Override
    public void load(ConfigurationNode config) {}

    @Override
    public void display(Pane pane) {
        ItemBuilder builder = new ItemBuilder(this.displayItem.copy());

        builder.name(UtilChatColour.translateColourCodes('&',
                this.huntConfig.getDisplayName().replace("%species%", this.currentPokemon.getDisplayName())));

        for (String s : this.huntConfig.getPreLore()) {
            builder.addLore(UtilChatColour.translateColourCodes('&', s.replace("%time%",
                                                                               UtilTimeFormat.getFormattedDuration((this.currentStart + this.duration) - System.currentTimeMillis()))));
        }

        builder.lore(this.currentPokemon.getDescription("§a", "§b").stream().map(StringTextComponent::new).collect(Collectors.toList()));

        for (String s : this.huntConfig.getExtraLore()) {
            builder.addLore(UtilChatColour.translateColourCodes('&', s.replace("%time%",
                    UtilTimeFormat.getFormattedDuration((this.currentStart + this.duration) - System.currentTimeMillis()))));
        }

        builder.addLore(UtilChatColour.translateColourCodes('&', this.rewardDescription).toArray(new String[0]));

        pane.set(this.guiX, this.guiY, GuiFactory.displayableBuilder(ItemStack.class)
                .itemStack(builder.build()).build());
    }

    @Override
    public PokemonSpec generatePokemon() {
        this.currentPokemon = this.generator.generate();
        this.displayItem = this.currentPokemon.getPhoto();
        PixelHuntStartEvent event = new PixelHuntStartEvent(this, this.currentPokemon);

        Pixelmon.EVENT_BUS.post(event);

        this.currentStart = System.currentTimeMillis();
        this.currentPokemon = event.getGeneratedPokemon();

        for (String broadcast : this.huntConfig.getSpawnBroadcast()) {
            ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(
                    new StringTextComponent(UtilChatColour.translateColourCodes('&', broadcast
                            .replace("%pokemon%", this.currentPokemon.getDisplayName()))), ChatType.CHAT, Util.NIL_UUID);
        }

        return this.currentPokemon;
    }

    @Override
    public boolean isBeingHunted(Pokemon pokemon) {
        if (this.currentPokemon == null || pokemon == null) {
            return false;
        }

        return this.currentPokemon.matches(pokemon);
    }

    @Override
    public boolean isSpeciesHunted(Pokemon pokemon) {
        if (this.currentPokemon == null || pokemon == null) {
            return false;
        }

        return this.currentPokemon.doesSpeciesMatch(pokemon);
    }

    @Override
    public void rewardCatch(EnvyPlayer<?> player, Pokemon caught) {
        ServerPlayerEntity parent = (ServerPlayerEntity) player.getParent();
        PixelHuntWonEvent wonEvent = new PixelHuntWonEvent(this, parent, caught, this.currentPokemon);

        Pixelmon.EVENT_BUS.post(wonEvent);

        if (this.ivMultiplierEnabled) {
            for (BattleStatsType value : BattleStatsType.values()) {
                if (value == BattleStatsType.NONE || value == BattleStatsType.ACCURACY || value == BattleStatsType.EVASION) {
                    continue;
                }

                caught.getIVs().setStat(value, Math.min(31, (int) (caught.getIVs().getStat(value) * this.ivMultiplier)));
            }
        } else if (this.maxIvs) {
            for (BattleStatsType value : BattleStatsType.values()) {
                if (value == BattleStatsType.NONE || value == BattleStatsType.ACCURACY || value == BattleStatsType.EVASION) {
                    continue;
                }

                caught.getIVs().setStat(value, 31);
            }
        }

        UtilForgeConcurrency.runSync(() -> {
            if (this.randomCommands) {
                UtilForgeServer.executeCommand(UtilRandom.getRandomElement(this.rewardCommands).replace("%player%", parent.getName().getString()));
            } else {
                for (String rewardCommand : this.rewardCommands) {
                    UtilForgeServer.executeCommand(rewardCommand.replace("%player%", parent.getName().getString()));
                }
            }
        });
    }

    @Override
    public void end() {
        if (this.currentPokemon == null) {
            return;
        }

        for (String message : this.huntConfig.getTimeoutBroadcast()) {
            ServerLifecycleHooks.getCurrentServer().getPlayerList()
                    .broadcastMessage(new StringTextComponent(UtilChatColour.translateColourCodes('&',
                            message.replace("%pokemon%",
                            this.currentPokemon.getDisplayName()))), ChatType.CHAT, Util.NIL_UUID);
        }
    }

    @Override
    public boolean hasTimedOut() {
        long timePassed = System.currentTimeMillis() - this.currentStart;

        return timePassed >= this.duration;
    }
}
