package com.xpgaming.pixelhunt.hunt;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.xpgaming.pixelhunt.utils.pokemon.PokemonSpec;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;

/**
 *
 * An interface to represent the different hunts available on a server
 *
 */
public interface PixelHunt {

    /**
     *
     * Identifier of the hunt rarity
     *
     * @return The hunt's identifier
     */
    String getIdentifier();

    /**
     *
     * Loads the hunt from the {@link ConfigurationNode}
     *
     * @param config The node the hunt is being loaded from
     */
    void load(ConfigurationNode config);

    /**
     *
     * Get the display item for the hunt UI
     *
     * @return the display item for the pixelhunt GUI
     */
    ItemStack getDisplay();

    /**
     *
     * Randomly generates the next pokemon for this hunt's rarity.
     *
     * @return The specification of the new pokemon to be hunted
     */
    PokemonSpec generatePokemon();

    /**
     *
     * Checks if a specific pokemon is currently the target for this hunt
     *
     * @param pokemon The target pokemon
     * @return if it's to be hunted or not
     */
    boolean isBeingHunted(Pokemon pokemon);

    /**
     *
     * Rewards the player for catching the hunted pokemon
     *
     * @param player The player who won the hunt
     */
    void rewardCatch(EntityPlayerMP player, Pokemon caught);

    /**
     *
     * Force end the hunt
     *
     */
    void end();

    /**
     *
     * Check if the hunt has timed out (i.e. no one caught the specified pokemon in time)
     *
     * @return true if timed out - false if not
     */
    boolean hasTimedOut();

}
