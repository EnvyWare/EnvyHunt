package com.envyful.pixel.hunt.remastered.forge.ui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.items.ItemBuilder;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class HuntUI {

    public static void open(EnvyPlayer<EntityPlayerMP> player) {
        ConfigInterface guiConfig = PixelHuntForge.getInstance().getConfig().getConfigInterface();
        Pane pane = GuiFactory.paneBuilder()
                .topLeftY(0)
                .topLeftX(0)
                .height(guiConfig.getHeight())
                .width(9)
                .build();

        for (ConfigItem fillerItem : guiConfig.getFillerItems()) {
            List<String> lore = Lists.newArrayList();

            for (String s : fillerItem.getLore()) {
                lore.add(UtilChatColour.translateColourCodes('&', s));
            }

            pane.add(GuiFactory.displayableBuilder(ItemStack.class)
                    .itemStack(new ItemBuilder()
                            .type(Item.getByNameOrId(fillerItem.getType()))
                            .name(UtilChatColour.translateColourCodes('&', fillerItem.getName()))
                            .lore(lore)
                            .damage(fillerItem.getDamage())
                            .amount(fillerItem.getAmount())
                            .build())
                    .build());
        }

        for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
            hunt.display(pane);
        }

        GuiFactory.guiBuilder()
                .height(guiConfig.getHeight())
                .title(UtilChatColour.translateColourCodes('&', guiConfig.getTitle()))
                .addPane(pane)
                .setCloseConsumer(envyPlayer -> {})
                .setPlayerManager(PixelHuntForge.getInstance().getPlayerManager())
                .build().open(player);
    }
}
