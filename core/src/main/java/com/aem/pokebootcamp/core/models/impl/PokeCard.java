package com.aem.pokebootcamp.core.models.impl;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO Class PokeCard.
 */
@Model(adaptables = Resource.class,
        adapters = {PokeCard.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class PokeCard {

    @Getter
    @ValueMapValue
    private String name;

    @Getter
    @ValueMapValue
    private String fileReference;

    @ValueMapValue
    private List<String> pokemonTypes;

    /**
     * Returns a list of simplified Pokémon type names.
     *
     * <p>This method strips the namespace or path from the tag values,
     * returning only the last segment. For example:
     * <pre>
     *     "pokemon:types/fire" → "fire"
     * </pre></p>
     *
     * @return list of Pokémon type names without prefixes or tag namespaces
     */
    public List<String> getPokemonTypes() {
        return pokemonTypes.stream()
                .map(type -> type.substring(type.lastIndexOf(':') + 1))
                .collect(Collectors.toList());
    }
}
