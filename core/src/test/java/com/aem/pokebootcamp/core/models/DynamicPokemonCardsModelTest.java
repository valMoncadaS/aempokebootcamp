package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Test class for validating the behavior of the DynamicPokemonCardsModel class.
 * Focuses on testing the initialization logic and population of the Pokemon list
 * using mocked dependencies, specifically the PokemonAPIService.
 *
 * Methods:
 * - setup: Prepares the test environment by configuring mocked dependencies and setting
 *   private fields using reflection.
 * - testPokemonListPopulation: Verifies the proper initialization of the Pokemon list in
 *   the model based on the predefined number of Pokemon, ensuring that the service is called
 *   and data is correctly set.
 *
 * Annotations:
 * - @ExtendWith(MockitoExtension.class): Enables the usage of Mockito annotations in
 *   the test class, such as @Mock and @InjectMocks.
 * - @InjectMocks: Injects mocked dependencies into the test instance
 *   of DynamicPokemonCardsModel.
 * - @Mock: Mocks the PokemonAPIService to simulate external dependency behavior.
 */
@ExtendWith(MockitoExtension.class)
class DynamicPokemonCardsModelTest {

    private static final int POKEMON_NUMBER = 3;
    private static final int POKEMON_NUMBER_EMPTY = 0;
    private static final int POKEMON_NUMBER_MAX = 1025;

    @InjectMocks
    private DynamicPokemonCardsModel model;

    @Mock
    private PokemonAPIService pokemonAPIService;

    @Test
    void testPokemonListPopulation() throws NoSuchFieldException {
        PrivateAccessor.setField(model, "numberOfPokemon", POKEMON_NUMBER);
        when(pokemonAPIService.getPokemon(anyInt())).thenReturn(PokemonDTO.builder().build());
        model.init();
        assertEquals(POKEMON_NUMBER, model.getPokemonCardsList().size(),
                "Expected number of Pokemon cards to be populated");
    }

    @Test
    void testPokemonListEmpty() throws NoSuchFieldException {
        PrivateAccessor.setField(model, "numberOfPokemon", POKEMON_NUMBER_EMPTY);
        model.init();
        assertEquals(POKEMON_NUMBER_EMPTY, model.getPokemonCardsList().size(),
                "Expected number of Pokemon cards to be populated");
    }

    @Test
    void testPokemonListMoreThanAllowed() throws NoSuchFieldException {
        PrivateAccessor.setField(model, "numberOfPokemon", POKEMON_NUMBER_MAX + 1);
        model.init();
        assertEquals(POKEMON_NUMBER_EMPTY, model.getPokemonCardsList().size(),
                "Expected number of Pokemon cards to be populated");
    }
}

