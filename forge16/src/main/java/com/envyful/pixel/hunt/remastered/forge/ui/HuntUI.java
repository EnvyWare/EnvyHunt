package com.envyful.pixel.hunt.remastered.forge.ui;

import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntGraphics;
import com.envyful.pixel.hunt.remastered.forge.ui.transformer.HuntTransformer;

public class HuntUI {

    private HuntUI() {
        throw new UnsupportedOperationException("Static UI class");
    }

    public static void open(ForgeEnvyPlayer player, int page) {
        PixelHuntGraphics.HuntUI guiConfig = PixelHuntForge.getGraphics().getHuntUI();

        Pane pane = GuiFactory.paneBuilder()
                .topLeftY(0)
                .topLeftX(0)
                .height(guiConfig.getGuiSettings().getHeight())
                .width(9)
                .build();

        UtilConfigInterface.fillBackground(pane, guiConfig.getGuiSettings());

        for (PixelHuntConfig.HuntConfig hunt : PixelHuntForge.getConfig().getHunts()) {
            if (page != hunt.getPage()) {
                continue;
            }

            UtilConfigItem.builder()
                    .singleClick()
                    .asyncClick()
                    .clickHandler((envyPlayer, clickType) -> {
                        if (hunt.isAllowRewardUI()) {
                            RewardUI.open(player, hunt, 1);
                        }
                    })
                    .extendedConfigItem(player, pane, hunt.getDisplayItem(), new HuntTransformer(hunt));
        }

        if (page != guiConfig.getPages()) {
            UtilConfigItem.builder()
                    .asyncClick()
                    .clickHandler((envyPlayer, clickType) -> open(player, page + 1))
                    .extendedConfigItem(player, pane, guiConfig.getNextPage());
        }

        if (page != 1) {
            UtilConfigItem.builder()
                    .asyncClick()
                    .clickHandler((envyPlayer, clickType) -> open(player, page - 1))
                    .extendedConfigItem(player, pane, guiConfig.getPreviousPage());
        }

        GuiFactory.guiBuilder()
                .height(guiConfig.getGuiSettings().getHeight())
                .title(UtilChatColour.colour(guiConfig.getGuiSettings().getTitle()))
                .addPane(pane)
                .setPlayerManager(PixelHuntForge.getPlayerManager())
                .build().open(player);
    }
}
