package com.aem.pokebootcamp.core.bean;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Pokemon card model with details such as the name, image, and types of a Pokemon.
 * It is adaptable from a Sling Resource and uses optional default injection strategies.
 * Provides methods to get Pokemon details and process its types into a human-readable format.
 */
@Model(adaptables = {Resource.class, HttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCard {
    @Getter
    @ValueMapValue
    private String name;

    @Getter
    @ValueMapValue
    private String pokemonImage;

    @ValueMapValue
    private List<String> type;

    /**
     * Method to return a list of types in a human-readable format.
     * @return list of types
     */
    public List<String> getType() {
        return type.stream()
                .map(typeElement -> StringUtils.capitalize(typeElement.split(":")[1]))
                .sorted()
                .collect(Collectors.toList());
    }
}
