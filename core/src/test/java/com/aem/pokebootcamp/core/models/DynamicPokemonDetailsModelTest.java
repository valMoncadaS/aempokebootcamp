package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit test class for the {@code DynamicPokemonDetailsModel}.
 * This test class covers various scenarios to ensure the correct functionality of the model,
 * particularly its interaction with the {@link PokemonAPIService} for retrieving dynamic Pokemon details.
 * Test Scenarios:
 * - Valid Pokemon ID: Verifies the model is initialized with details fetched from the external service.
 * - Null or Invalid Pokemon ID: Validates the model's behavior when no data is available or the ID is invalid.
 * Mocked Dependencies:
 * - {@link PokemonAPIService}: The service responsible for providing the Pokemon details.
 * Fields:
 * - {@code POKEMON_ID}: A constant representing a sample Pokemon ID used for testing.
 * - {@code POKEMON_ID_TEXT}: The field name representing the Pokemon ID in the model,
 * used for private field access during tests.
 * - {@code model}: The test instance of the {@link DynamicPokemonDetailsModel}, injected with mocks.
 * - {@code pokemonAPIService}: The mocked service for fetching Pokemon information.
 * Methods:
 * - {@code testInitWithValidPokemonId()}: Tests the successful initialization of the model with a valid Pokemon ID.
 * - {@code testInitWithNullPokemonId()}: Tests the model's behavior when no or invalid Pokemon ID is provided.
 */
@ExtendWith(MockitoExtension.class)
class DynamicPokemonDetailsModelTest {
    private static final int POKEMON_ID = 25;
    private static final int POKEMON_ID_LESS_ALLOWED = 0;
    private static final int POKEMON_ID_MAX_ALLOWED = 1026;

    private static final String POKEMON_ID_TEXT = "pokemonId";
    private static final String POKEMON_HEIGHT_NULL =  "Expected Pokemon height to be null";
    private static final String POKEMON_WEIGHT_NULL = "Expected Pokemon weight to be null";
    private static final String POKEMON_CATEGORY_NULL = "Expected Pokemon category to be null";
    private static final String POKEMON_GENDER_NULL = "Expected Pokemon gender to be null";
    private static final String POKEMON_ABILITY_NULL = "Expected Pokemon ability list to be null";
    private static final String POKEMON_POKEDEX_NULL = "Expected Pokemon Pokedex link to be null";

    @InjectMocks
    private DynamicPokemonDetailsModel model;

    @Mock
    private PokemonAPIService pokemonAPIService;

    @Test
    void initWithValidPokemonId() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID);
        final PokemonDTO dto = PokemonDTO.builder()
                .height("0.4 m")
                .weight("6.0 kg")
                .category("Mouse")
                .gender(Arrays.asList("male", "female"))
                .abilities(Arrays.asList("Static", "Lightning Rod"))
                .name("pikachu")
                .build();

        when(pokemonAPIService.getPokemon(POKEMON_ID)).thenReturn(dto);
        when(pokemonAPIService.getExternalLink()).thenReturn("https://www.pokemon.com/us/pokedex");

        model.init();

        assertEquals("0.4 m", model.getHeight(), "Expected Pokemon height to be '0.4 m'");
        assertEquals("6.0 kg", model.getWeight(), "Expected Pokemon weight to be '6.0 kg'");
        assertEquals("Mouse", model.getCategory(), "Expected Pokemon category to be 'Mouse'");
        assertEquals(Arrays.asList("male", "female"), model.getGender(),
                "Expected Pokemon gender to be 'male, female'");
        assertEquals("https://www.pokemon.com/us/pokedex/pikachu", model.getPokedexLink(),
                "Expected Pokemon Pokedex link to be 'https://www.pokemon.com/us/pokedex/pikachu'");

        assertNotNull(model.getAbilities(),
                "Expected Pokemon ability list to be populated from the test resource");
        assertEquals(2, model.getAbilities().size(),
                "Expected 2 Pokemon abilities to be loaded from the test resource");
        assertEquals("Static", model.getAbilities().get(0).getAbilityName(),
                "Expected Pokemon ability name to be 'Static'");
    }

    @Test
    void initWithNullPokemonId() {
        model.init();

        assertNull(model.getHeight(), POKEMON_HEIGHT_NULL);
        assertNull(model.getWeight(), POKEMON_WEIGHT_NULL);
        assertNull(model.getCategory(), POKEMON_CATEGORY_NULL);
        assertNull(model.getGender(), POKEMON_GENDER_NULL);
        assertNull(model.getAbilities(), POKEMON_ABILITY_NULL);
        assertNull(model.getPokedexLink(), POKEMON_POKEDEX_NULL);
    }

    @Test
    void initWithLessAllowedPokemonId() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID_LESS_ALLOWED);
        model.init();

        assertNull(model.getHeight(), POKEMON_HEIGHT_NULL);
        assertNull(model.getWeight(), POKEMON_WEIGHT_NULL);
        assertNull(model.getCategory(), POKEMON_CATEGORY_NULL);
        assertNull(model.getGender(), POKEMON_GENDER_NULL);
        assertNull(model.getAbilities(), POKEMON_ABILITY_NULL);
        assertNull(model.getPokedexLink(), POKEMON_POKEDEX_NULL);
    }

    @Test
    void initWithMaxAllowedPokemonId() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID_MAX_ALLOWED);
        model.init();

        assertNull(model.getHeight(), POKEMON_HEIGHT_NULL);
        assertNull(model.getWeight(), POKEMON_WEIGHT_NULL);
        assertNull(model.getCategory(), POKEMON_CATEGORY_NULL);
        assertNull(model.getGender(), POKEMON_GENDER_NULL);
        assertNull(model.getAbilities(), POKEMON_ABILITY_NULL);
        assertNull(model.getPokedexLink(), POKEMON_POKEDEX_NULL);
    }

    @Test
    void initWithPokemonAPIServiceNull() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID);
        PrivateAccessor.setField(model, "pokemonAPIService", null);
        model.init();

        assertNull(model.getHeight(), POKEMON_HEIGHT_NULL);
        assertNull(model.getWeight(), POKEMON_WEIGHT_NULL);
        assertNull(model.getCategory(), POKEMON_CATEGORY_NULL);
        assertNull(model.getGender(), POKEMON_GENDER_NULL);
        assertNull(model.getAbilities(), POKEMON_ABILITY_NULL);
        assertNull(model.getPokedexLink(), POKEMON_POKEDEX_NULL);
    }
}
