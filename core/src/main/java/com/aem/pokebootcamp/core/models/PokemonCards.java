package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.models.impl.PokeCard;

import java.util.List;

/**
 * Interface representing a collection of Pokemon card data.
 *
 * <p>This interface exposes access to a list of {@link PokeCard} objects,
 * which may include various properties such as name, type, stats, or image metadata,
 * depending on the implementation.</p>
 */
public interface PokemonCards {
    /**
     * Returns a list of Pokemon card models.
     *
     * @return a list of {@link PokeCard} instances representing individual Pokémon cards
     */
    List<PokeCard> getPokemonCards();
}
