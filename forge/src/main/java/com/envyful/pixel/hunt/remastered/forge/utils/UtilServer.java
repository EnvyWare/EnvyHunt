package com.envyful.pixel.hunt.remastered.forge.utils;

import com.envyful.pixel.hunt.remastered.forge.PixelHuntForge;

public class UtilServer {

    public static void executeCommand(String command) {
        PixelHuntForge.getServer().getCommandManager().executeCommand(PixelHuntForge.getServer(), command);
    }
}
