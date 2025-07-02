package com.aem.pokebootcamp.core.models;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import javax.inject.Inject;
import java.util.*;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import java.io.Serializable;

/**
 *  Pokémon Card Model with name, tags and fileReference.
 */
@Slf4j
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCardModelList extends SlingSafeMethodsServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @ValueMapValue
    private String name;

    @Getter
    @ValueMapValue
    private String fileReference;

    @Getter
    @ValueMapValue
    private String[] tags;

    @Inject
    @Self
    private Resource resource;


    /**
    * Function returns a list of tag names.
     * @return list of tag names that were selected from the data source dropdown
     * or an empty list if no tags were selected.
     */
    public List<String> getTagNames() {
        final List<String> tagNames = new ArrayList<>();
        final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        if (tags != null && tagManager != null) {
            for (final String tagItem : tags) {
                final Tag tag = tagManager.resolve(tagItem);
                if (tag != null) {
                    tagNames.add(tag.getTitle());
                } else {
                    log.warn("Tag not found!");
                }
            }
        } else {
            log.warn("tags array is empty or TagManager is null");
        }
        return tagNames;
    }
}
