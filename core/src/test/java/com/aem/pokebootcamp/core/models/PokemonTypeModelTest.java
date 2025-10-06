package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.services.TagXFService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void pokemonWeaknessWithContent() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonweakness");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonTypeModel model = request.adaptTo(PokemonTypeModel.class);
        assertNotNull(model, "Expected Pokemon Type to be adapted to from the test resource");
        assertEquals("Weakness", model.getTitle(), "Expected Pokemon Type title to be 'Weakness'");
    }

    @Test
    void pokemonTypeExperienceFragmentsWithContent() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonweakness");
        final TagXFService tagXFService = mock(TagXFService.class);
        context.registerService(TagXFService.class, tagXFService);
        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonTypeModel model = request.adaptTo(PokemonTypeModel.class);

        when(tagXFService.getXFsByTags(List.of("pokemon-types:dragon", "pokemon-types:fairy")))
                .thenReturn(List.of("/content/experience-fragments/aempokebootcamp/us/en/site/type/dragon",
                        "/content/experience-fragments/aempokebootcamp/us/en/site/type/fairy"));
        assertNotNull(model, "Expected Pokemon Type to be adapted from the test resource");
        assertEquals("/content/experience-fragments/aempokebootcamp/us/en/site/type/dragon",
                model.getExperienceFragments().get(0), "Expected Pokemon type to be dragon XF path");
        assertEquals("/content/experience-fragments/aempokebootcamp/us/en/site/type/fairy",
                model.getExperienceFragments().get(1), "Expected Pokemon type to be fairy XF path");
    }

}
