package com.envyful.pixel.hunt.remastered.forge.commands;


import com.envyful.acaf.api.command.Command;
import com.envyful.acaf.api.command.Permissible;
import com.envyful.acaf.api.command.executor.Argument;
import com.envyful.acaf.api.command.executor.CommandProcessor;
import com.envyful.acaf.api.command.executor.Sender;
import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;
import com.envyful.pixel.hunt.remastered.forge.config.PixelHuntConfig;
import com.envyful.pixel.hunt.remastered.forge.hunt.PixelHuntFactory;
import com.envyful.pixel.hunt.remastered.api.PixelHunt;
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
        HuntUI.open(sender);
    }

    @CommandProcessor("reload")
    @Permissible("com.envyful.pixel.hunt.remastered.forge.pixelhunt.admin.reload")
    public void executeReloadCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(STARTED_RELOAD);
        PixelHuntForge.getInstance().setConfig(PixelHuntConfig.getInstance(PixelHuntConfig.CONFIG_PATH));
        PixelHuntFactory.reloadAll();
        sender.sendMessage(RELOADED);
    }

    @CommandProcessor("start")
    @Permissible("com.envyful.pixel.hunt.remastered.forge.pixelhunt.admin.start")
    public void executeStartCommand(@Sender EntityPlayerMP sender, @Argument PixelHunt target) {
        sender.sendMessage(STARTED);

        if (!target.hasTimedOut()) {
            target.end();
        }

        target.generatePokemon();
    }

    @CommandProcessor("list")
    @Permissible("com.envyful.pixel.hunt.remastered.forge.pixelhunt.admin.list")
    public void executeListCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(new TextComponentString("Hunts: "));
        for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
            sender.sendMessage(new TextComponentString("  " + hunt.getIdentifier()));
        }
    }
}

