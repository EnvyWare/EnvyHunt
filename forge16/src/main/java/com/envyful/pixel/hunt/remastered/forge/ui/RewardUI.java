package com.envyful.pixel.hunt.remastered.forge.ui;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.gui.close.ForgeCloseConsumer;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;

public class RewardUI {

    public static void open(ForgeEnvyPlayer player, PixelHuntConfig.HuntConfig hunt, int page) {
        PixelHuntConfig.HuntRewardUI rewardUI = hunt.getRewardUI();

        Pane pane = GuiFactory.paneBuilder()
                .topLeftY(0)
                .topLeftX(0)
                .height(rewardUI.getGuiSettings().getHeight())
                .width(9)
                .build();

        UtilConfigInterface.fillBackground(pane, rewardUI.getGuiSettings());

        for (PixelHuntConfig.Reward entry : hunt.getRewardCommands().getWeightedSet().keySet()) {
            if (page != entry.getPage()) {
                continue;
            }

            UtilConfigItem.builder()
                    .extendedConfigItem(player, pane, entry.getDisplayItem());
        }

        if (page != rewardUI.getPages()) {
            UtilConfigItem.builder()
                    .asyncClick()
                    .clickHandler((envyPlayer, clickType) -> open(player, hunt, page + 1))
                    .extendedConfigItem(player, pane, rewardUI.getNextPage());
        }

        if (page != 1) {
            UtilConfigItem.builder()
                    .asyncClick()
                    .clickHandler((envyPlayer, clickType) -> open(player, hunt, page - 1))
                    .extendedConfigItem(player, pane, rewardUI.getPreviousPage());
        }

        GuiFactory.guiBuilder()
                .height(rewardUI.getGuiSettings().getHeight())
                .title(UtilChatColour.colour(rewardUI.getGuiSettings().getTitle()))
                .addPane(pane)
                .closeConsumer(GuiFactory.closeConsumerBuilder()
                        .async()
                        .delayTicks(1)
                        .handler(envyPlayer -> HuntUI.open(player, 1))
                        .build())
                .setPlayerManager(PixelHuntForge.getPlayerManager())
                .build().open(player);
    }
}
