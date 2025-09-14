package com.aem.pokebootcamp.core.bean;

import com.aem.pokebootcamp.core.services.TagXFService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Getter
    @ValueMapValue
    private List<String> type;

    @OSGiService
    private TagXFService tagXFService;


    /**
     * Retrieves the list of Pokemon types associated with the current Pokemon card.
     * This method resolves the types by using the tagXFService to fetch
     * the corresponding Experience Fragments based on the tags defined for the Pokemon.
     * @return a list of XFs paths as strings, extracted from the associated tags
     */
    public List<String> getTypes() {
        return tagXFService.getXFsByTags(type);
    }
}
