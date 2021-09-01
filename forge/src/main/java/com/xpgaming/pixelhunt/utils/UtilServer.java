package com.xpgaming.pixelhunt.utils;

import com.xpgaming.pixelhunt.PixelHuntForge;

public class UtilServer {

    public static void executeCommand(String command) {
        PixelHuntForge.getServer().getCommandManager().executeCommand(PixelHuntForge.getServer(), command);
    }
}
