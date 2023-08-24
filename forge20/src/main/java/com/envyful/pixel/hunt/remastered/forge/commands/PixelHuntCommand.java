package com.envyful.pixel.hunt.remastered.forge.commands;


import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import com.envyful.pixel.hunt.remastered.forge.ui.HuntUI;
import net.minecraft.server.level.ServerPlayer;

@Command(
       value = "hunt",
        description = "A command to open the PixelHunt UI",
        aliases = {
                "pixelhunt",
                "pokemonhunt",
                "pokehunt",
                "hunts",
                "pixelhunts",
                "pokemonhunts",
                "pokehunts"
        }
)
@SubCommands(ReloadCommand.class)
public class PixelHuntCommand {

    @CommandProcessor
    public void executeCommand(@Sender ServerPlayer sender) {
        HuntUI.open(EnvyHunt.getPlayerManager().getPlayer(sender), 1);
    }
}

