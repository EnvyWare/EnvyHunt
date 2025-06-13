package com.envyful.pixel.hunt.remastered.forge.ui;

import com.envyful.api.gui.pane.Pane;
import com.envyful.api.neoforge.config.UtilConfigItem;
import com.envyful.api.neoforge.player.ForgeEnvyPlayer;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntGraphics;
import com.envyful.pixel.hunt.remastered.forge.ui.transformer.HuntTransformer;

public class HuntUI {

    private HuntUI() {
        throw new UnsupportedOperationException("Static UI class");
    }

    public static void open(ForgeEnvyPlayer player, int page) {
        PixelHuntGraphics.HuntUI guiConfig = EnvyHunt.getGraphics().getHuntUI();

        Pane pane = guiConfig.getGuiSettings().toPane();

        for (var hunt : EnvyHunt.getConfig().getHunts()) {
            if (page != hunt.getPage() || !hunt.isEnabled()) {
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

        pane.open(player, guiConfig.getGuiSettings());
    }
}
