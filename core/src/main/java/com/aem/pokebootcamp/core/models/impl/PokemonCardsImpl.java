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

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {PokemonCards.class},
        resourceType = {PokemonCardsImpl.RESOURCE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class PokemonCardsImpl implements PokemonCards {
    public static final String RESOURCE = "aempokebootcamp/components/pokemoncards";

    @Getter
    @ChildResource(name = "pokemonCards")
    private Resource pokemonCards;

    private List<PokemonItem> pokemonItemList;

    @PostConstruct
    protected void inint() {
        if (pokemonCards != null) {
            pokemonItemList = StreamSupport.stream(pokemonCards.getChildren().spliterator(), false)
                    .map(resource -> resource.adaptTo(PokemonItem.class))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<PokemonItem> getPokemonCards() {
        return pokemonItemList;
    }
}