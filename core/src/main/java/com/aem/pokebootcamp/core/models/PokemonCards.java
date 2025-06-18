package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.models.impl.PokeCard;

import java.util.List;

/**
 * Interface to handle PokemonCards properties.
 */
public interface PokemonCards {
    /**
     * getPokemonTypes.
     * @return List
     */
    List<PokeCard> getPokemonCards();
}
