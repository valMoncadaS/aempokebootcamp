package com.aem.pokebootcamp.core.models.impl;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class,
        adapters = {PokeCard.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokeCard {

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
