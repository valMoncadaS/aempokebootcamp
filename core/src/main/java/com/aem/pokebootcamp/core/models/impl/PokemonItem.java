package com.aem.pokebootcamp.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, 
    adapters = {PokemonItem.class}, 
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public final class PokemonItem {

    @Getter
    @ValueMapValue
    private String name;

    @Getter
    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private List<String> types;


    public List<String> getTypes() {
        return types.stream()
                    .map(type -> type.substring(type.lastIndexOf(':') + 1))
                    .collect(Collectors.toList());
    }
}