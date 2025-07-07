package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link PokemonCardsImpl}.
 * <p>
 * This test class uses {@link AemContext} from wcm.io to simulate an AEM environment
 * and validate the behavior of the {@link PokemonCards} model implementation.
 * </p>
 */
@Slf4j
@ExtendWith(AemContextExtension.class)
class PokemonCardsImplTest {
    private final AemContext context = new AemContext();
    private PokemonCards pokemonCards;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(PokemonCardsImpl.class, PokemonItem.class);
        context.load().json("/com/aem/pokebootcamp/core/models/impl/pokemon-cards.json", "/content");
        context.currentResource("/content/pokemoncards");
        pokemonCards = context.request().adaptTo(PokemonCards.class);
    }

    @Test
    void pokemonCardsTest() {
        final int expectedItems = 2;
        final String expectedName = "Bulbasaur";
        final List<String> expectedPokemonTypes = Arrays.asList("poison");

        assertEquals(
                expectedName,
                pokemonCards.getPokemonCards().get(0).getName(),
                "Expected the name of the first Pokémon card to be '" + expectedItems + "'");
        assertEquals(
                expectedPokemonTypes,
                pokemonCards.getPokemonCards().get(0).getTypes(),
                "Expected the Pokémon types of the first card to be " + expectedPokemonTypes);
        assertEquals(
                expectedItems,
                pokemonCards.getPokemonCards().size(),
                "Expected total number of Pokémon cards to be " + expectedItems);
    }

    @Test
    void testInitWhenPokemonCardsResourceIsNull() {
    // Simulate resource without pokemonCards child
    context.load().json("/com/aem/pokebootcamp/core/models/impl/pokemon-cards-empty.json", "/content-empty");
    context.currentResource("/content-empty/pokemoncards");

    final PokemonCards model = context.request().adaptTo(PokemonCards.class);

    assertNotNull(model, "Expected not null model");
    assertNotNull(model.getPokemonCards(), "Expected not null list even when no child resource");

    // Either it's an empty list or a default behavior — check the size
    assertEquals(0, model.getPokemonCards().size(), "Expected list to be empty when pokemonCards child is missing");
    }
}
