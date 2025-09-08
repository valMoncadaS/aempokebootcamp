package com.aem.pokebootcamp.core.models;

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
 * Unit test for the PokemonTypeModel Sling model, verifying its behavior and integration
 * with AEM components. This test ensures the model correctly adapts and provides expected
 * output for title and type weaknesses.
 *
 *
 * Key test scenarios covered:
 * - Validation of the model's ability to adapt from a resource.
 * - Retrieval and validation of title (type/weaknesses) from the model using mock resources.
 * - Ensures proper loading and adaptation of JSON test content.
 *
 * Dependencies:
 * - The test classes and components leverage the io.wcm.testing.mock.aem.junit5.AemContextExtension
 *   for AEM context and request mocking.
 */
@ExtendWith(AemContextExtension.class)
class PokemonTypeModelTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/models/PokemonTypeModelTest.json",
                "/content");
    }

    @Test
    void pokemonTypeWithContent() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemontype");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonTypeModel model = request.adaptTo(PokemonTypeModel.class);
        assertNotNull(model, "Expected Pokemon Type to be adapted to from the test resource");
        assertEquals("Type", model.getTitle(), "Expected Pokemon Type title to be 'Type'");

        final List<String> pokemonTypes = List.copyOf(model.getTypesWeaknesses());
        assertNotNull(pokemonTypes, "Expected Pokemon Type list to be populated from the test resource");
        assertEquals("pokemon-types:dragon", pokemonTypes.get(0), "Expected Pokemon Type to be 'dragon'");
        assertEquals("pokemon-types:fairy", pokemonTypes.get(1), "Expected Pokemon Type to be 'fairy'");
        assertEquals("pokemon-types:bug", pokemonTypes.get(2), "Expected Pokemon Type to be 'bug'");
    }

    @Test
    void pokemonWeaknessWithContent() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonweakness");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonTypeModel model = request.adaptTo(PokemonTypeModel.class);
        assertNotNull(model, "Expected Pokemon Type to be adapted to from the test resource");
        assertEquals("Weakness", model.getTitle(), "Expected Pokemon Type title to be 'Weakness'");

        final List<String> pokemonTypes = List.copyOf(model.getTypesWeaknesses());
        assertNotNull(pokemonTypes, "Expected Pokemon Type list to be populated from the test resource");
        assertEquals("pokemon-types:dragon", pokemonTypes.get(0), "Expected Pokemon Type to be 'dragon'");
        assertEquals("pokemon-types:fairy", pokemonTypes.get(1), "Expected Pokemon Type to be 'fairy'");
    }

}
