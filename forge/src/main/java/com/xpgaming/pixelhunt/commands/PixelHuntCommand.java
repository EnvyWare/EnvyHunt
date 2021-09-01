package com.xpgaming.pixelhunt.commands;


import com.envyful.acaf.api.command.Command;
import com.envyful.acaf.api.command.Permissible;
import com.envyful.acaf.api.command.executor.Argument;
import com.envyful.acaf.api.command.executor.CommandProcessor;
import com.envyful.acaf.api.command.executor.Sender;
import com.xpgaming.pixelhunt.PixelHuntForge;
import com.xpgaming.pixelhunt.config.PixelHuntConfig;
import com.xpgaming.pixelhunt.hunt.PixelHunt;
import com.xpgaming.pixelhunt.hunt.PixelHuntFactory;
import com.xpgaming.pixelhunt.ui.HuntUI;
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
    @Permissible("pixelhunt.admin.reload")
    public void executeReloadCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(STARTED_RELOAD);
        PixelHuntForge.getInstance().setConfig(PixelHuntConfig.getInstance(PixelHuntConfig.CONFIG_PATH));
        PixelHuntFactory.reloadAll();
        sender.sendMessage(RELOADED);
    }

    @CommandProcessor("start")
    @Permissible("pixelhunt.admin.start")
    public void executeStartCommand(@Sender EntityPlayerMP sender, @Argument PixelHunt target) {
        sender.sendMessage(STARTED);

        if (!target.hasTimedOut()) {
            target.end();
        }

        target.generatePokemon();
    }

    @CommandProcessor("list")
    @Permissible("pixelhunt.admin.list")
    public void executeListCommand(@Sender EntityPlayerMP sender) {
        sender.sendMessage(new TextComponentString("Hunts: "));
        for (PixelHunt hunt : PixelHuntFactory.getAllHunts()) {
            sender.sendMessage(new TextComponentString("  " + hunt.getIdentifier()));
        }
    }
}

