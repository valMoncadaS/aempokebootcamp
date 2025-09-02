package com.aem.pokebootcamp.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Class to add the details of a Pokémon.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonDetailsModel {
    @Getter
    @ValueMapValue
    private Double height;

    @Getter
    @ValueMapValue
    private String category;

    @Getter
    @ValueMapValue
    private Double weight;

    @Getter
    @ValueMapValue
    private String[] ability;

    @Getter
    @ValueMapValue
    private String[] gender;

    /**
     * @return a boolean depending on the details, if they have all data or no.
     */
    public boolean isEmpty() {
        return height == null && category == null
                && weight == null && ability == null
                && gender == null;
    }
}
