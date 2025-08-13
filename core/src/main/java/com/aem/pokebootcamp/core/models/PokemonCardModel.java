package com.aem.pokebootcamp.core.models;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;
/**
 * Class to create a multifield List of PokemonCards.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonCardModel {
    @Getter
    @ChildResource
    private List<PokemonCardModelList> multifieldList;
}
