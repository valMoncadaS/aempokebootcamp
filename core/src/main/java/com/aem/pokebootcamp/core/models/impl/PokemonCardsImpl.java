package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = PokemonCards.class,
    resourceType = PokemonCardsImpl.RESOURCE_TYPE,
    defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL

)
public class PokemonCardsImpl implements PokemonCards {
  protected static final String RESOURCE_TYPE = "aempokebootcamp/components/pokemoncards";

  @Getter
  @ChildResource
  private List<PokemonCardImpl> pokemonCards;
}
