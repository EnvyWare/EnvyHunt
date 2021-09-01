package com.envyful.pixel.hunt.remastered.api.factory;

import com.envyful.pixel.hunt.remastered.api.PixelHunt;

import java.util.List;

/**
 *
 * An interface to represent a platform specific implementation of hunt factory
 *
 */
public interface PixelHuntPlatformFactory {

    /**
     *
     * Reloads all hunts from config
     *
     */
    void reloadHunts();

    /**
     *
     * Gets all active loaded hunts
     *
     * @return all active loaded hunts
     */
    List<PixelHunt> getAllHunts();

}
