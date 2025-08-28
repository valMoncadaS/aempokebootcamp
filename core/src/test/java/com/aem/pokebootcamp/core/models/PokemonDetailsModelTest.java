package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.bean.PokemonAbility;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for the PokemonDetailsModel.
 *
 * This class ensures that the PokemonDetailsModel correctly adapts resources
 * and provides accurate data from its fields. It uses the AEM Context framework
 * to simulate the AEM environment and load test data.
 *
 * The tests cover various scenarios, including:
 * - PokemonDetailsModel with all properties populated.
 * - PokemonDetailsModel with an empty abilities list.
 *
 * Each test validates that the model correctly retrieves the intended data
 * and that the underlying logic behaves as expected.
 *
 * Annotations used:
 * - @ExtendWith(AemContextExtension.class): Extends this test class with
 *   AEM context support.
 *
 * Dependencies:
 * - io.wcm.testing.mock.aem.junit5.AemContext: Simulates the AEM environment.
 * - org.junit.jupiter.api: Provides the testing framework for writing the tests.
 */
@ExtendWith(AemContextExtension.class)
class PokemonDetailsModelTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/models/PokemonDetailsModelTest.json",
                "/content");
    }

    @Test
    void pokemonDetailsModelWithAllProperties() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonDetails");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonDetailsModel model = request.adaptTo(PokemonDetailsModel.class);

        assertNotNull(model, "Expected PokemonDetailsModel to be adapted to from the test resource");
        assertEquals("4' 07\"", model.getHeight(), "Expected Pokemon height to be '4' 07\"");
        assertEquals("136.7 lbs", model.getWeight(), "Expected Pokemon weight to be '136.7 lbs'");
        assertEquals("Drill", model.getCategory(), "Expected Pokemon category to be 'Drill'");
        assertEquals(List.of("male", "female"), model.getGender(), "Expected Pokemon gender to be 'male, female'");

        final List<PokemonAbility> pokemonAbilities = List.copyOf(model.getAbilities());
        assertNotNull(pokemonAbilities, "Expected Pokemon ability list to be populated from the test resource");
        assertEquals(2, pokemonAbilities.size(), "Expected 2 Pokemon abilities to be loaded from the test resource");
    }

    @Test
    void pokemonDetailsModelWithEmptyAbilities() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonDetails-empty-abilities");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonDetailsModel model = request.adaptTo(PokemonDetailsModel.class);

        assertNotNull(model, "Expected PokemonDetailsModel to be adapted to from the test resource");
        final List<PokemonAbility> pokemonAbilities = List.copyOf(model.getAbilities());
        assertNotNull(pokemonAbilities, "Expected Pokemon ability list to be populated from the test resource");
        assertTrue(pokemonAbilities.isEmpty(), "Expected PokemonCard list to be empty");
    }
}
