package com.envyful.pixel.hunt.remastered.forge.ui;

import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.neoforge.chat.UtilChatColour;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.pixel.hunt.remastered.forge.config.HuntConfig;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;

public class RewardUI {

    private RewardUI() {
        throw new UnsupportedOperationException("Static UI class");
    }

    public static void open(ForgeEnvyPlayer player, HuntConfig hunt, int page) {
        PixelHuntConfig.HuntRewardUI rewardUI = hunt.getRewardUI();

        Pane pane = rewardUI.getGuiSettings().toPane();

        for (ExtendedConfigItem entry : hunt.getRewardDisplayItems()) {

            UtilConfigItem.builder()
                    .extendedConfigItem(player, pane, entry);
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
                .build().open(player);
    }
}
