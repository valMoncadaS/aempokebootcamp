package com.aem.pokebootcamp.core.models.impl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link PokemonStatsImpl}.
 * Verifies that each stat is correctly mapped from the JSON resource.
 */

@ExtendWith(AemContextExtension.class)
class PokemonStatsImplTest {

    private final AemContext context = new AemContext();
    private PokemonStatsImpl model;
    private static final int EXPECTED_HP = 80;
    private static final int EXPECTED_ATTACK = 100;
    private static final int EXPECTED_DEFENSE = 90;
    private static final int EXPECTED_SPECIAL_ATTACK = 110;
    private static final int EXPECTED_SPECIAL_DEFENSE = 95;
    private static final int EXPECTED_SPEED = 120;

    @BeforeEach
    void setUp() {
        context.addModelsForPackage("com.aem.pokebootcamp.core.models.impl");

        context.load().json("/com/aem/pokebootcamp/core/models/impl/pokemon-stats.json", "/content/pokemon");

        final Resource resource = context.resourceResolver().getResource("/content/pokemon");

        assertNotNull(resource, "Resource should not be null");
        model = resource.adaptTo(PokemonStatsImpl.class);
    }

    @Test
    void testStats() {
        assertNotNull(model, "Model should not be null");

        assertEquals(EXPECTED_HP, model.getHitPoints(), "Hit Points mismatch");
        assertEquals(EXPECTED_ATTACK, model.getAttack(), "Attack mismatch");
        assertEquals(EXPECTED_DEFENSE, model.getDefense(), "Defense mismatch");
        assertEquals(EXPECTED_SPECIAL_ATTACK, model.getSpecialAttack(), "Special Attack mismatch");
        assertEquals(EXPECTED_SPECIAL_DEFENSE, model.getSpecialDefense(), "Special Defense mismatch");
        assertEquals(EXPECTED_SPEED, model.getSpeed(), "Speed mismatch");
    }
}