package com.aem.pokebootcamp.core.bean;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test class for the {@link PokemonAbility} model.
 *
 * This class verifies the functionality of the {@link PokemonAbility} class by
 * adapting a mock resource using the AEM context and asserting the expected ability name.
 * It ensures the model adapts correctly and provides accurate data when accessed.
 *
 * Key methods tested:
 * - {@code getAbilityName}: Ensures the ability name is correctly fetched from the resource.
 *
 * Dependencies:
 * - AEM Mocks {@link AemContext} for simulating the AEM environment.
 * - JSON test data for resource content at "/com/aem/pokebootcamp/core/beans/PokemonAbilityBeanTest.json".
 *
 * Annotations:
 * - {@link ExtendWith}: Integrates with AEM context for testing.
 * - {@link Test}: Marks test methods for execution.
 * - {@link BeforeEach}: Executes setup logic before each test.
 */
@ExtendWith(AemContextExtension.class)
class PokemonAbilityBeanTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/beans/PokemonAbilityBeanTest.json",
                "/content");
    }

    @Test
    void pokemonAbilityWithAbilityName() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonAbility");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonAbility pokemonAbility = request.adaptTo(PokemonAbility.class);

        assertNotNull(pokemonAbility, "Expected PokemonAbility to be adapted to from the test resource");
        assertEquals("Poison Point", pokemonAbility.getAbilityName(),
                "Expected Pokemon ability name to be 'Poison Point'");
    }
}
