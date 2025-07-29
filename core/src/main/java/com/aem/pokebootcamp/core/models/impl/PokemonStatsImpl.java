package com.aem.pokebootcamp.core.models.impl;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.aem.pokebootcamp.core.models.PokemonStats;

import javax.inject.Inject;

@Model(
    adaptables = Resource.class,
    adapters = PokemonStats.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PokemonStatsImpl  implements PokemonStats {

    @Getter
    @ValueMapValue
    private int hp;

    @Getter
    @ValueMapValue
    private int attack;

    @Getter
    @ValueMapValue
    private int defense;

    @Getter
    @ValueMapValue
    private int specialAttack;

    @Getter
    @ValueMapValue
    private int specialDefense;

    @Getter
    @ValueMapValue
    private int speed;
}