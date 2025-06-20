package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;
/**
 * Default implementation of the {@link PokemonCards} interface.
 *
 * <p>This model adapts from a {@link SlingHttpServletRequest} and is used to expose
 * a list of Pokemon card items authored under the component's node structure in AEM.</p>
 *
 * <p>It expects child nodes representing individual Pokemon cards,
 * which are automatically mapped to {@link PokeCard} models.</p>
 */
@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {PokemonCards.class},
        resourceType = {PokemonCardsImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCardsImpl implements PokemonCards {
    /**
     * Resource type for the Pokemon cards component.
     */
    public static final String RESOURCE_TYPE = "aempokebootcamp/components/pokemoncards";

    @Getter
    @ChildResource
    private List<PokeCard> pokemonCards;
}
