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
    void getPokemonCardsSizeTest() {
        final int cards = 2;
        assertEquals(cards, pokemonCards.getPokemonCards().size());
    }

    @Test
    void getPokemonCardsValuesTest() {
        final String name = "Bulbasaur";
        final List<String> pokemonTypes = Arrays.asList("poison");

        assertEquals(name, pokemonCards.getPokemonCards().get(0).getName());
        assertEquals(pokemonTypes, pokemonCards.getPokemonCards().get(0).getPokemonTypes());
    }
}