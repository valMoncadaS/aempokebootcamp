package com.aem.pokebootcamp.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class,
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

    /**
     * Returns a cleaned list of tag names without namespace.
     *
     * @return list of tag titles
     */

    public List<String> getTypes() {
        if (types == null) {
            return List.of();
        }
        return types.stream()
                .map(type -> type.substring(type.lastIndexOf(':') + 1))
                .collect(Collectors.toList());
    }
}