package com.envyful.pixel.hunt.remastered.forge.commands;


import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.pixel.hunt.remastered.api.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.ui.HuntUI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

@Command(
       value = "hunt",
        description = "A command to open the PixelHunt UI",
        aliases = {
               "pixelhunt",
               "pokemonhunt",
               "pokehunt"
        }
)
public class PixelHuntCommand {

    private static final ITextComponent STARTED_RELOAD = new TextComponentString("Reloading...");
    private static final ITextComponent RELOADED = new TextComponentString("Reloaded");
    private static final ITextComponent STARTED = new TextComponentString("Attempting to start hunt");

    @CommandProcessor
    public void executeCommand(@Sender EntityPlayerMP sender) {
        HuntUI.open(PixelHuntForge.getInstance().getPlayerManager().getPlayer(sender));
    }

    @CommandProcessor("reload")
    @Permissible("pixel.hunt.remastered.command.reload")
    public void executeReloadCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(STARTED_RELOAD);
        PixelHuntForge.getInstance().loadConfig();
        PixelHuntFactory.reloadHunts();
        sender.sendMessage(RELOADED);
    }
}

