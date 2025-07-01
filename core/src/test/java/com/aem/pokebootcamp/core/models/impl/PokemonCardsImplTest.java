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
    private final AemContext ctx = new AemContext();
    private PokemonCards pokemonCards;

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(PokemonCardsImpl.class, PokeCard.class);
        ctx.load().json("/com/aem/pokebootcamp/core/models/impl/PokemonCardsImplTest.json", "/content");
        ctx.currentResource("/content/pokemoncards");
        pokemonCards = ctx.request().adaptTo(PokemonCards.class);
    }

    @Test
    void pokemonCardsTest() {
        final int expectedCards = 2;
        final String expectedName = "Bulbasaur";
        final List<String> expectedPokemonTypes = Arrays.asList("poison");

        assertEquals(
                expectedName,
                pokemonCards.getPokemonCards().get(0).getName(),
                "Expected the name of the first Pokémon card to be '" + expectedCards + "'");
        assertEquals(
                expectedPokemonTypes,
                pokemonCards.getPokemonCards().get(0).getPokemonTypes(),
                "Expected the Pokémon types of the first card to be " + expectedPokemonTypes);
        assertEquals(
                expectedCards,
                pokemonCards.getPokemonCards().size(),
                "Expected total number of Pokémon cards to be " + expectedCards);
    }
}