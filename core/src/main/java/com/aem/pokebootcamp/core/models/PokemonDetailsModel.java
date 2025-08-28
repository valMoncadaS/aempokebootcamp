package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.bean.PokemonAbility;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * The PokemonDetailsModel class represents a Sling Model used for detailing specific attributes of a Pokémon.
 * It is adaptable from a Sling Resource or a Sling HTTP servlet request and is associated with the
 * resource type `aempokebootcamp/components/pokemondetails`.
 *
 * This model provides access to various fields describing a Pokémon, such as its height, weight, category, abilities,
 * and gender. It utilizes Sling Models annotations to inject these properties from the underlying repository.
 *
 * Fields:
 * - `height`: Represents the height of the Pokémon.
 * - `weight`: Represents the weight of the Pokémon.
 * - `category`: Represents the category of the Pokémon.
 * - `abilities`: A collection of abilities associated with the Pokémon, mapped as child resources.
 * - `gender`: Represents the gender variations of the Pokémon as a list of strings.
 *
 * Methods:
 * - `init`: Initializes non-mandatory fields like abilities to ensure they are never null,
 * replacing null values with empty collections.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        resourceType = PokemonDetailsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonDetailsModel {

    /**
     * Defines the resource type associated with the `PokemonDetailsModel`.
     * This constant represents the path of the AEM component `aempokebootcamp/components/pokemondetails`,
     * enabling the model to be linked to the corresponding resource in the repository.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/pokemondetails";
    @Getter
    @ValueMapValue
    private String height;

    @Getter
    @ValueMapValue
    private String weight;

    @Getter
    @ValueMapValue
    private String category;

    @Getter
    @ChildResource
    private Collection<PokemonAbility> abilities;

    @Getter
    @ValueMapValue
    private List<String> gender;

    /**
     * Method created to initialize a list as empty if it is null.
     */
    @PostConstruct
    protected void init() {
        abilities = CollectionUtils.emptyIfNull(abilities);
    }
}
