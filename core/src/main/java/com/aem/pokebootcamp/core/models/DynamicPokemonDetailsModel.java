package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.bean.PokemonAbility;
import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Model class for representing dynamic details of a Pokemon.
 * This model is adaptable from a Sling Resource, and its data is injected with optional
 * strategies. It integrates with the {@link PokemonAPIService} to fetch dynamic Pokemon
 * details based on the `pokemonId` provided in the component configuration.
 * Key Features:
 * - Retrieves and stores height, weight, category, abilities, gender, and Pokedex link of a Pokemon.
 * - Supports a maximum Pokemon ID limit defined by {@code MAX_POKEMON_NUMBER}.
 * - Processes and formats the Pokemon's abilities into {@link PokemonAbility} objects.
 * Fields:
 * - `pokemonId`: Specifies the unique Pokemon ID for which details are retrieved.
 * - `height`: Contains the Pokemon's height, fetched via the API.
 * - `weight`: Stores the Pokemon's weight, fetched from external data.
 * - `category`: Describes the category of the Pokemon.
 * - `abilities`: Contains a list of the Pokemon's abilities represented as {@link PokemonAbility} objects.
 * - `gender`: Stores genders applicable to the Pokemon.
 * - `pokedexLink`: Holds the complete URL linking to the Pokemon's Pokedex entry.
 * The model uses the {@link PostConstruct} lifecycle to fetch and initialize the data during
 * component creation.
 */
@Model(
        adaptables = Resource.class,
        resourceType = DynamicPokemonDetailsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicPokemonDetailsModel {

    /**
     * Defines the resource type associated with the `DynamicPokemonDetailsModel`.
     * Specifies the path for the AEM component `aempokebootcamp/components/dynamicpokemondetails`,
     * enabling this model to be linked to the corresponding resource in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/dynamicpokemondetails";
    private static final int MAX_POKEMON_NUMBER = 1025;

    @ValueMapValue
    private int pokemonId;

    @OSGiService
    private PokemonAPIService pokemonAPIService;

    @Getter
    private String height;

    @Getter
    private String weight;

    @Getter
    private String category;

    @Getter
    private List<PokemonAbility> abilities;

    @Getter
    private List<String> gender;

    @Getter
    private String pokedexLink;

    /**
     * Initializes the model with detailed information about a Pokemon based on its unique ID.
     * This method is executed after the model's construction and performs the following steps:
     * - Validates whether the `pokemonId` is within the valid range
     * (greater than 0 and less than or equal to `MAX_POKEMON_NUMBER`)
     *   and ensures the `pokemonAPIService` is not null.
     * - Retrieves the Pokemon data using the `pokemonAPIService` and populates model fields:
     *   - `height` and `weight`: Assigned from the retrieved Pokemon's height and weight.
     *   - `category` and `gender`: Assigned from the retrieved Pokemon's category and gender.
     *   - `pokedexLink`: Constructed using the external link provided by the API and the Pokemon's name.
     *   - `abilities`: Converted from the list of ability names to a list of `PokemonAbility` objects.
     * If the `pokemonId` or `pokemonAPIService` do not meet the above conditions,
     * the method does not perform any initialization.
     */
    @PostConstruct
    protected void init() {
        if (pokemonId > 0 && pokemonId <= MAX_POKEMON_NUMBER && pokemonAPIService != null) {
            final PokemonDTO dto = pokemonAPIService.getPokemon(pokemonId);

            height = dto.getHeight();
            weight = dto.getWeight();
            category = dto.getCategory();
            gender = dto.getGender();
            pokedexLink = pokemonAPIService.getExternalLink() + "/" + dto.getName();

            // Convert ability names to PokemonAbility objects
            abilities = CollectionUtils.emptyIfNull(dto.getAbilities()).stream()
                    .map(name -> {
                        final PokemonAbility ability = new PokemonAbility();
                        ability.setAbilityName(name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1));
                        return ability;
                    })
                    .collect(Collectors.toList());
        }
    }
}
