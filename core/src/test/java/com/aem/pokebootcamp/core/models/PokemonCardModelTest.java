package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.bean.PokemonCard;
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
 * Unit tests for the PokemonCardModel class.
 *
 * This test class uses JUnit 5 and the AEM Mocks framework to test the functionality
 * of the PokemonCardModel. It ensures that the model is properly adaptable from the
 * AEM resource or request and that the expected behavior of the model matches the
 * defined requirements.
 *
 * Test scenarios:
 * - `pokemonCardModelWithCards`: Tests the behavior when the model contains multiple Pokemon cards.
 *   Verifies that all fields are populated as expected and matches the mocked input JSON data.
 * - `pokemonCardModelWithEmptyCards`: Tests the behavior when the model contains an empty list
 *   of Pokemon cards. Verifies that the list is not null and returns an empty collection.
 */
@ExtendWith(AemContextExtension.class)
class PokemonCardModelTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/models/PokemonCardModelTest.json",
                "/content");
    }

    @Test
    void pokemonCardModelWithCards() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemoncards");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonCardModel model = request.adaptTo(PokemonCardModel.class);

        assertNotNull(model, "Expected PokemonCardModel to be adapted to from the test resource");
        final List<PokemonCard> cards = List.copyOf(model.getPokemonCardsList());
        assertNotNull(cards, "Expected PokemonCard list to be populated from the test resource");
        assertEquals(2, cards.size(), "Expected 2 Pokemon cards to be loaded from the test resource");

        final PokemonCard card2 = cards.get(0);
        assertEquals("Bulbasaur", card2.getName(), "Expected Pokemon name to be 'Bulbasaur'");
        assertEquals("/content/dam/aempokebootcamp/pokemon-images/001.png", card2.getPokemonImage(),
                "Expected Pokemon image to be '/content/dam/aempokebootcamp/pokemon-images/001.png'");
        assertEquals(List.of("Grass", "Poison"), card2.getType(),
                "Expected Pokemon types to be 'Grass, Poison'");

        final PokemonCard card1 = cards.get(1);
        assertEquals("Pikachu", card1.getName(), "Expected Pokemon name to be 'Pikachu'");
        assertEquals("/content/dam/aempokebootcamp/pokemon-images/025.png", card1.getPokemonImage(),
                "Expected Pokemon image to be '/content/dam/aempokebootcamp/pokemon-images/025.png'");
        assertEquals(List.of("Electric"), card1.getType(), "Expected Pokemon types to be 'Electric'");
    }

    @Test
    void pokemonCardModelWithEmptyCards() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemoncards-empty-list");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonCardModel model = request.adaptTo(PokemonCardModel.class);

        assertNotNull(model, "Expected PokemonCardModel to be adapted to from the test resource");
        final List<PokemonCard> cards = List.copyOf(model.getPokemonCardsList());
        assertNotNull(cards, "Expected PokemonCard list to be populated from the test resource");
        assertTrue(cards.isEmpty(), "Expected PokemonCard list to be empty");
    }
}
