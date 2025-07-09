package com.aem.pokebootcamp.core.bean;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PokemonCard} class.
 * This test class verifies the functionality of the PokemonCard properties,
 * including the name, image, and types of a Pokemon, by adapting mock resources
 * using the AEM context and ensuring the expected outputs match the actual values.
 */
@ExtendWith(AemContextExtension.class)
class PokemonCardBeanTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // Load any common test resources if needed
        context.load().json("/com/aem/pokebootcamp/core/beans/PokemonCardBeanTest.json",
                "/content");
    }

    @Test
    void pokemonCardWithAllProperties() {
        final Resource resource = context.resourceResolver().getResource("/content/pokemonCard");

        context.currentResource(resource);
        final MockSlingHttpServletRequest request = context.request();
        final PokemonCard pokemonCard = request.adaptTo(PokemonCard.class);


        assertNotNull(pokemonCard, "Expected PokemonCard to be adapted to from the test resource");
        assertEquals("Bulbasaur", pokemonCard.getName(), "Expected Pokemon name to be 'Bulbasaur'");
        assertEquals("/content/dam/aempokebootcamp/pokemon-images/001.png", pokemonCard.getPokemonImage(),
                "Expected Pokemon image to be '/content/dam/aempokebootcamp/pokemon-images/001.png'");

        final List<String> expectedTypes = Arrays.asList("Grass", "Poison");
        assertEquals(expectedTypes, pokemonCard.getType(), "Expected Pokemon types to be 'Grass, Poison'");
    }
}
