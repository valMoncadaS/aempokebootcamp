package com.aem.pokebootcamp.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The PokemonStatsModel class is a Sling Model used to represent the stats of a Pokémon.
 * It is adaptable from a Sling Resource or SlingHttpServletRequest and is mapped to the
 * resource type `aempokebootcamp/components/pokemonstats`.
 *
 * This model captures various attributes of a Pokémon's statistics, such as hit points,
 * attack, defense, special attack, special defense, and speed. These properties are injected
 * using the {@link ValueMapValue} injector from the corresponding resource.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        resourceType = "aempokebootcamp/components/pokemonstats",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonStatsModel {
    @Getter
    @ValueMapValue
    private String healthPoints;

    @Getter
    @ValueMapValue
    private String attack;

    @Getter
    @ValueMapValue
    private String defense;

    @Getter
    @ValueMapValue
    private String specialAttack;

    @Getter
    @ValueMapValue
    private String specialDefense;

    @Getter
    @ValueMapValue
    private String speed;
}
