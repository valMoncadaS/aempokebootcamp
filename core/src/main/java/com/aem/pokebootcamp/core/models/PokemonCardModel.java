package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.bean.PokemonCard;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * Represents a model for a collection of Pokemon cards.
 *
 * This class is adaptable from Sling resources or a Sling HTTP servlet request
 * and is associated with the AEM component `aempokebootcamp/components/pokemoncards`.
 * The model utilizes optional injection strategies to fetch child resources.
 *
 * This model provides access to a collection of `PokemonCard` objects, which
 * represent individual Pokemon cards. If the collection of Pokemon cards is
 * null during initialization, it will be replaced with an empty collection.
 *
 * Fields:
 * - `pokemonCardsList`: A collection of `PokemonCard` objects retrieved from child resources.
 *
 * Methods:
 * - `init`: Initializes the model by ensuring the collection of Pokemon cards is never null.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        resourceType = "aempokebootcamp/components/pokemoncards",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCardModel {
    @Getter
    @ChildResource
    private Collection<PokemonCard> pokemonCardsList;

    /**
     * Method created to initialize a class as empty if it is null.
     */
    @PostConstruct
    protected void init() {
        pokemonCardsList = CollectionUtils.emptyIfNull(pokemonCardsList);
    }
}
