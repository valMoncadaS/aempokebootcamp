package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * DynamicPokemonStatsModel is a Sling Model used to dynamically fetch and represent
 * the stats of a Pokémon by interacting with an external Pokémon API service.
 * This model extends the functionality of PokemonStatsModel by adding dynamic loading
 * of Pokémon stats using the Pokémon's unique identifier (ID). It retrieves the stats
 * from a service and populates fields such as health points, attack, defense,
 * special attack, special defense, and speed.
 * The model is adaptable from a Sling Resource and is associated with the resourceType
 * `aempokebootcamp/components/dynamicpokemonstats`.
 * Key Functionalities:
 * - Receives a Pokémon ID as input and dynamically fetches the corresponding stats.
 * - Fetches and processes data from an external API service (`PokemonAPIService`).
 * - Populates various Pokémon stats with the fetched data if available.
 */
@Slf4j
@Model(
        adaptables = Resource.class,
        resourceType = DynamicPokemonStatsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicPokemonStatsModel {

    /**
     * Defines the resource type associated with the `DynamicPokemonStatsModel`.
     * Specifies the AEM component path `aempokebootcamp/components/dynamicpokemonstats`,
     * linking this model to the corresponding resource type in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/dynamicpokemonstats";
    private static final int MAX_POKEMON_NUMBER = 1025;


    @ValueMapValue
    private int pokemonId;

    @OSGiService
    private PokemonAPIService pokemonAPIService;

    @Getter
    private String healthPoints;

    @Getter
    private String attack;

    @Getter
    private String defense;

    @Getter
    private String specialDefense;

    @Getter
    private String specialAttack;

    @Getter
    private String speed;


    /**
     * Initializes the dynamic Pokemon stats model by retrieving the statistics
     * of a Pokemon identified by its unique ID from the Pokemon API service.
     * This method is annotated with {@code @PostConstruct} to indicate that it
     * should be invoked after the dependency injection is complete. It checks
     * whether the required dependencies, such as the Pokemon ID and Pokemon API
     * service, are available. If these dependencies are present, it fetches the
     * Pokemon's information from the service and maps its statistics to the
     * corresponding fields, such as health points, attack, defense, special attack,
     * special defense, and speed.
     * No action is taken if the required dependencies are missing.
     */
    @PostConstruct
    protected void init() {
        if (pokemonId > 0 && pokemonId <= MAX_POKEMON_NUMBER && pokemonAPIService != null) {
            final PokemonDTO pokemonDTO = pokemonAPIService.getPokemon(pokemonId);
            final Map<String, Integer> stats = pokemonDTO.getStats();

            healthPoints = getStatValue(stats, "hp");
            attack = getStatValue(stats, "attack");
            defense = getStatValue(stats, "defense");
            specialAttack = getStatValue(stats, "special-attack");
            specialDefense = getStatValue(stats, "special-defense");
            speed = getStatValue(stats, "speed");
        }
    }

    /**
     * Retrieves the value of a specified statistic from the provided map.
     * If the key does not exist in the map or the map itself is null, this method returns null.
     *
     * @param stats a map containing statistic names as keys and their corresponding integer values
     * @param key the name of the statistic to retrieve from the map
     * @return the value of the specified statistic as a string, or null if the key is not found or the map is null
     */
    private String getStatValue(final Map<String, Integer> stats, final String key) {
        return stats != null && stats.containsKey(key) ? String.valueOf(stats.get(key)) : null;
    }
}
