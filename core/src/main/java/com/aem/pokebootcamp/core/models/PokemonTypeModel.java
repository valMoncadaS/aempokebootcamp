package com.aem.pokebootcamp.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * Model used for representing and interacting with Pokémon type-related information within an AEM component.
 * This model is tied to the resource type `aempokebootcamp/components/pokemontype` and provides methods
 * for managing and retrieving data associated with Pokémon type weaknesses and corresponding Experience Fragments (XF).
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        resourceType = PokemonTypeModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonTypeModel {
    /**
     * Defines the resource type associated with the `PokemonTypeModel`.
     * Specifies the path for the AEM component `aempokebootcamp/components/pokemontype`,
     * enabling this model to be linked to the corresponding resource in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/pokemontype";

    @Getter
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue
    private List<String> typesWeaknesses;
}
