package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCard;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class,
    adapters = PokemonCard.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCardImpl implements PokemonCard {

  @Getter
  @ValueMapValue
  String name;

  @Getter
  @ValueMapValue
  String fileReference;

  @ValueMapValue
  List<String> pokemonTypes;

  public List<String> getPokemonTypes() {
    return pokemonTypes.stream()
        .map(type -> type.substring(type.lastIndexOf(':') + 1))
        .collect(Collectors.toList());
  }
}
