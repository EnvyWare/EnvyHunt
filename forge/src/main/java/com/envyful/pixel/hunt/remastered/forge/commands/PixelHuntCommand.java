package com.envyful.pixel.hunt.remastered.forge.commands;


import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
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
    @Permissible("pixel.hunt.remastered.command.reload")
    public void executeReloadCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(STARTED_RELOAD);
        //TODO: reload
        sender.sendMessage(RELOADED);
    }

    @CommandProcessor("start")
    @Permissible("pixel.hunt.remastered.command.start")
    public void executeStartCommand(@Sender EntityPlayerMP sender, @Argument PixelHunt target) {
        sender.sendMessage(STARTED);

        if (!target.hasTimedOut()) {
            target.end();
        }

        target.generatePokemon();
    }
}

