package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dynamic model for loading a configurable number of Pokemon cards.
 * The class is adaptable from Sling resources and is associated with the AEM component
 * `aempokebootcamp/components/dynamicpokemoncards`. It dynamically fetches a list of Pokemon
 * using the `PokemonAPIService` based on the configured number of Pokemon to retrieve.
 *
 * Fields:
 * - `RESOURCE_TYPE`: Defines the resource type associated with this model.
 * - `MAX_POKEMON_NUMBER`: Specifies the maximum number of Pokemon that can be retrieved.
 * - `numberOfPokemon`: Stores the number of Pokemon set to be displayed.
 * - `pokemonAPIService`: Dependency-injected service used to fetch Pokemon data.
 * - `pokemonList`: A list of `PokemonDTO` objects containing the detailed data for each fetched Pokemon.
 *
 * Methods:
 * - `init`: Initializes the model by fetching Pokemon data from the `PokemonAPIService`.
 *    If `numberOfPokemon` is set between 1 and `MAX_POKEMON_NUMBER`, an attempt is made
 *    to retrieve the specified number of Pokemon. If exceptions occur during the fetching
 *    of individual Pokemon, they are logged and skipped to ensure continuity of the list population.
 */
@Model(
        adaptables = Resource.class,
        resourceType = DynamicPokemonCardsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicPokemonCardsModel {

    /**
     * Defines the resource type associated with the `DynamicPokemonCardsModel`.
     * Specifies the path for the AEM component `aempokebootcamp/components/dynamicpokemoncards`,
     * enabling this model to be linked to the corresponding resource in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/dynamicpokemoncards";
    private static final int MAX_POKEMON_NUMBER = 1025;

    @ValueMapValue
    private int numberOfPokemon;

    @OSGiService
    private PokemonAPIService pokemonAPIService;

    @Getter
    private List<PokemonDTO> pokemonCardsList;

    /**
     * Initializes the model by fetching a list of Pokemon data.
     * If the configured `numberOfPokemon` is within the valid range
     * (1 to `MAX_POKEMON_NUMBER`), this method attempts to retrieve
     * the specified number of Pokemon using the `PokemonAPIService`.
     * Any exceptions encountered during data retrieval are caught, logged,
     * and skipped to ensure the process continues smoothly.
     */
    @PostConstruct
    protected void init() {
        pokemonCardsList = new ArrayList<>();
        if (numberOfPokemon > 0 && numberOfPokemon <= MAX_POKEMON_NUMBER) {
            for (int i = 1; i <= numberOfPokemon; i++) {
                pokemonCardsList.add(pokemonAPIService.getPokemon(i));
            }
        }
    }
}
