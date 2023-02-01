package com.envyful.pixel.hunt.remastered.forge.commands;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.pixel.hunt.remastered.forge.EnvyHunt;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "reload",
        description = "Reloads the config"
)
@Permissible("pixel.hunt.remastered.command.reload")
@Child
public class ReloadCommand {

    private static final ITextComponent STARTED_RELOAD = new StringTextComponent("Reloading...");
    private static final ITextComponent RELOADED = new StringTextComponent("Reloaded");

    @CommandProcessor
    public void executeCommand(@Sender ICommandSource sender) {
        sender.sendMessage(STARTED_RELOAD, Util.NIL_UUID);
        EnvyHunt.getInstance().loadConfig();
        sender.sendMessage(RELOADED, Util.NIL_UUID);
    }
}
