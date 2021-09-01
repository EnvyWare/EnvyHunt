package com.envyful.pixel.hunt.remastered.api;

import com.envyful.pixel.hunt.remastered.api.factory.PixelHuntPlatformFactory;

import java.util.List;

/**
 *
 * Static factory for managing {@link PixelHunt}s
 *
 */
public class PixelHuntFactory {

    private static PixelHuntPlatformFactory platformFactory = null;

    public static void setPlatformFactory(PixelHuntPlatformFactory platformFactory) {
        PixelHuntFactory.platformFactory = platformFactory;
    }

    public static void reloadHunts() {
        platformFactory.reloadHunts();
    }

    public static List<PixelHunt> getAllHunts() {
        return platformFactory.getAllHunts();
    }
}
