package com.aem.pokebootcamp.core.models;

import lombok.Getter;
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

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PokemonCardModelList extends SlingSafeMethodsServlet{
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

    public List<String> getTagNames(){
        List<String> tagNames = new ArrayList<>();
        TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        if(tags != null && tagManager != null){
            for(String tagItem : tags){
                Tag tag = tagManager.resolve(tagItem);
                if(tag != null) {
                    tagNames.add(tag.getTitle());
                }
            }
        }
        return tagNames;
    }
}
