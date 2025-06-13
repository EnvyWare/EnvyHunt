package com.envyful.pixel.hunt.remastered.forge.commands;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

@Command(
        value = "reload"
)
@Permissible("pixel.hunt.remastered.command.reload")
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
