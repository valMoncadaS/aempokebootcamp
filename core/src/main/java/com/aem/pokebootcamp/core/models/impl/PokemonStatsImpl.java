package com.aem.pokebootcamp.core.models.impl;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Getter
@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PokemonStatsImpl {

    @ValueMapValue
    private int hp;

    @ValueMapValue
    private int attack;

    @ValueMapValue
    private int defense;

    @ValueMapValue
    private int specialAttack;

    @ValueMapValue
    private int specialDefense;

    @ValueMapValue
    private int speed;
}