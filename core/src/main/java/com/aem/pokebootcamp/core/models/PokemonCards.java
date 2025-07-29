package com.aem.pokebootcamp.core.models;

import java.util.List;

import com.aem.pokebootcamp.core.models.impl.PokemonItem;

/**
 * Interface representing a collection of Pokemon card data.
 */

public interface PokemonCards {

    /**
     * Returns the list of Pokémon items from the component.
     *
     * @return a list of PokemonItem models
     */

    List<PokemonItem> getPokemonCards();
}

