package com.aem.pokebootcamp.core.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents a Pokemon's ability in the system.
 *
 * This model is adaptable from both a Sling Resource and an HTTP servlet request.
 * It uses an optional injection strategy and allows retrieval of the ability's name.
 *
 * Fields:
 * - `abilityName`: The name of the Pokemon's ability, retrieved via Sling's ValueMap.
 */
@Model(adaptables = {Resource.class, HttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PokemonAbility {
    @Getter
    @Setter
    @ValueMapValue
    private String abilityName;
}
