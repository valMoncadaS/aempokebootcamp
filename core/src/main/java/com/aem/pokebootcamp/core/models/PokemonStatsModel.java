package com.aem.pokebootcamp.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
/**
 * Class to create the statistics of a Pokémon.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonStatsModel {
    @Getter
    @ValueMapValue
    private Long hitPoints;

    @Getter
    @ValueMapValue
    private Long attack;

    @Getter
    @ValueMapValue
    private Long defense;

    @Getter
    @ValueMapValue
    private Long specialAttack;

    @Getter
    @ValueMapValue
    private Long specialDefense;

    @Getter
    @ValueMapValue
    private Long speed;
}
