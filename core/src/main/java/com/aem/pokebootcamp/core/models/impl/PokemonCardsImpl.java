package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import lombok.Getter;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, 
    adapters = {PokemonCards.class}, 
    resourceType = {PokemonCardsImpl.RESOURCE}, 
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class PokemonCardsImpl implements PokemonCards {
    public static final String RESOURCE = "aempokebootcamp/components/pokemoncards";

    @Getter
    @ChildResource
    private List<String> pokemonCards;
}