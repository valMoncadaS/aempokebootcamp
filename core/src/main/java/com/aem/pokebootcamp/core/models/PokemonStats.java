package com.aem.pokebootcamp.core.models;

/**
 * Represents Pokémon stats like HP, attack, and defense.
 */

public interface PokemonStats {

    /**
     * Returns the base HP stat of the Pokémon.
     *
     * @return the HP value
     */
    int getHitPoints();

    /**
     * Returns the attack stat.
     *
     * @return the attack value
     */
    int getAttack();

    /**
     * Returns the defense stat.
     *
     * @return the defense value
     */
    int getDefense();

    /**
     * Returns the special attack stat.
     *
     * @return the special attack value
     */
    int getSpecialAttack();

    /**
     * Returns the special defense stat.
     *
     * @return the special defense value
     */
    int getSpecialDefense();

    /**
     * Returns the speed stat.
     *
     * @return the speed value
     */
    int getSpeed();
}