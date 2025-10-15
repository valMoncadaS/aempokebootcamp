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
import java.util.List;

/**
 * Represents a model for dynamically retrieving and presenting Pokemon type data.*
 * This model integrates with the Adobe Experience Manager (AEM) Sling Models framework
 * and is associated with the resource type `aempokebootcamp/components/dynamicpokemontype`.
 * It utilizes dependency injection to manage resource properties and services, and initializes
 * its fields based on predefined conditions and external service responses.
 */
@Model(
        adaptables = Resource.class,
        resourceType = DynamicPokemonTypeModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicPokemonTypeModel {

    /**
     * Defines the resource type associated with the `DynamicPokemonTypeModel`.
     * Specifies the path for the AEM component `aempokebootcamp/components/dynamicpokemontype`,
     * enabling this model to be linked to the corresponding resource in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/dynamicpokemontype";

    private static final String WEAKNESS = "Weakness";
    private static final String TYPE = "Type";
    private static final int MAX_POKEMON_NUMBER = 1025;

    @ValueMapValue
    private int pokemonId;

    @Getter
    @ValueMapValue
    private String title;

    @OSGiService
    private PokemonAPIService pokemonAPIService;

    @Getter
    private List<String> typesWeaknesses;

    @Getter
    private List<String> experienceFragments;

    /**
     * Initializes the dynamic Pokemon type model.
     * This method is called after the dependency injections are performed. It performs the following tasks:
     * 1. Validates that the `pokemonId` is within the acceptable range
     * (greater than 0 and less than or equal to the maximum Pokemon number).
     * 2. Ensures that the `pokemonAPIService` is not null.
     * 3. Calls the `pokemonAPIService` to fetch Pokemon data corresponding to the ID.
     * 4. Depending on the `title` value, it sets the values of the `typesWeaknesses` and `experienceFragments` fields:
     *    - If `title` is equivalent to `Type`, the fields are populated with the Pokemon's types.
     *    - If `title` is equivalent to `Weakness`, the fields are populated with the Pokemon's weaknesses.
     */
    @PostConstruct
    protected void init() {
        if (pokemonId > 0 && pokemonId <= MAX_POKEMON_NUMBER && pokemonAPIService != null) {
            final PokemonDTO dto = pokemonAPIService.getPokemon(pokemonId);

            if (TYPE.equals(title)) {
                typesWeaknesses = dto.getTypes();
                experienceFragments = dto.getTypes();
            } else if (WEAKNESS.equals(title)) {
                typesWeaknesses = dto.getWeakness();
                experienceFragments = dto.getWeakness();
            }
        }
    }
}
