package com.aem.pokebootcamp.core.models;

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
 * Unit test for the {@code PokemonStatsModel} class.
 *
 * This test class verifies the functionality of the {@code PokemonStatsModel},
 * which represents the statistics of a Pokémon, including health points, attack,
 * defense, special attack, special defense, and speed. The test validates the mapping
 * of JSON data values to the model's fields and ensures the model is properly adapted
 * from a Sling resource.
 *
 * Responsibilities of this test include:
 * - Loading mock content from a JSON file into an AEM testing context.
 * - Adapting the mock resource to the {@code PokemonStatsModel}.
 * - Asserting the correct retrieval of all Pokémon statistics from the model.
 */
@ExtendWith(AemContextExtension.class)
class PokemonStatsModelTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/models/PokemonStatsModelTest.json",
                "/content");
    }

    @Test
    void pokemonStatsModelWithContent() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonstats");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonStatsModel model = request.adaptTo(PokemonStatsModel.class);
        assertNotNull(model, "Expected PokemonStats to be adapted to from the test resource");
        assertEquals("100", model.getHealthPoints(), "Expected Pokemon health points to be '100'");
        assertEquals("32", model.getAttack(), "Expected Pokemon attack to be '32'");
        assertEquals("43", model.getDefense(), "Expected Pokemon defense to be '43'");
        assertEquals("56", model.getSpecialAttack(), "Expected Pokemon special attack to be '56'");
        assertEquals("180", model.getSpecialDefense(), "Expected Pokemon special defense to be '180'");
        assertEquals("200", model.getSpeed(), "Expected Pokemon speed to be '200'");
    }
}
