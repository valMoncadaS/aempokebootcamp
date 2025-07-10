package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCards;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PokemonCardsImplTest {

    private final AemContext aemContext = new AemContext();
    PokemonCards pokemonCards;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCards.class);
        aemContext.load().json("src/test/resources/com/aem/pokebootcamp/core/models/impl/PokemonCardsTest.json", "/content");
    }

    @Test
    void getPokemonCards() {
        aemContext.currentResource("/content/pokemoncards");

        pokemonCards = aemContext.request().adaptTo(PokemonCards.class);
        final int expectedCards = 7;
        final String expectePokemonNameFirstCard = "Weedle";

        assertEquals(expectedCards, pokemonCards.getPokemonCards().size());
        assertEquals(expectePokemonNameFirstCard, pokemonCards.getPokemonCards().get(0).name);
    }
}