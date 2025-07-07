package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.api.resource.Resource;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import java.util.Collections;

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
        resourceType = {PokemonCardsImpl.RESOURCE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public final class PokemonCardsImpl implements PokemonCards {

    /** The resource type for the PokemonCards component. */
    public static final String RESOURCE = "aempokebootcamp/components/pokemoncards";

    @Getter
    @ChildResource(name = "pokemonCards")
    private Resource pokemonCards;

    private List<PokemonItem> pokemonItemList;

    /**
    * Initializes the list of Pokemon items. Subclasses may override this method to provide custom initialization.
    */

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @PostConstruct
    private void init() {
        if (pokemonCards != null) {
            pokemonItemList = StreamSupport.stream(pokemonCards.getChildren().spliterator(), false)
                    .map(resource -> resource.adaptTo(PokemonItem.class))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<PokemonItem> getPokemonCards() {
        return pokemonItemList != null ? pokemonItemList : Collections.emptyList();
    }
}