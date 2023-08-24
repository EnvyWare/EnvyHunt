package com.envyful.pixel.hunt.remastered.forge.commands;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

@Command(
        value = "reload",
        description = "Reloads the config"
)
@Permissible("pixel.hunt.remastered.command.reload")
@Child
public class ReloadCommand {

    private static final Component STARTED_RELOAD = Component.literal("Reloading...");
    private static final Component RELOADED = Component.literal("Reloaded");

    @CommandProcessor
    public void executeCommand(@Sender CommandSource sender) {
        sender.sendSystemMessage(STARTED_RELOAD);
        EnvyHunt.getInstance().loadConfig();
        sender.sendSystemMessage(RELOADED);
    }
}
