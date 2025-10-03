package com.aem.pokebootcamp.core.services;

import com.aem.pokebootcamp.core.dto.PokemonDTO;

/**
 * Service interface for retrieving Pokemon information from an external data source.
 * Provides methods to fetch details about a specific Pokemon using its unique identifier.
 */
public interface PokemonAPIService {
    /**
     * Retrieves detailed information about a specific Pokemon identified by its unique ID.
     * @param idPokemon the unique identifier of the Pokemon to be retrieved
     * @return a {@link PokemonDTO} containing detailed attributes of the specified Pokemon
     */
    PokemonDTO getPokemon(int idPokemon);

    /**
     * Retrieves the base URL used for accessing Pokemon data.
     * @return the base URL as a String that serves as the endpoint for fetching Pokemon information.
     */
    String getExternalLink();
}
