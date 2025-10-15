package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link DynamicPokemonTypeModel}.
 * Covers initialization logic and edge cases.
 */
@ExtendWith(MockitoExtension.class)
class DynamicPokemonTypeModelTest {

    private static final int VALID_POKEMON_ID = 25;
    private static final int POKEMON_ID_LESS_ALLOWED = 0;
    private static final int POKEMON_ID_MAX_ALLOWED = 1026;
    private static final String WEAKNESS = "Weakness";
    private static final String TYPE = "Type";
    private static final String TITLE = "title";
    private static final String POKEMON_ID = "pokemonId";
    private static final String WEAKNESS_NULL = "Expected weaknesses to be null";

    private static final List<String> MOCK_WEAKNESSES =
            List.of("/content/experience-fragments/aempokebootcamp/us/en/site/type/ground");
    private static final List<String> MOCK_TYPES =
            List.of("/content/experience-fragments/aempokebootcamp/us/en/site/type/electric");

    private static final PokemonDTO DTO = PokemonDTO.builder()
            .types(MOCK_TYPES)
            .weakness(MOCK_WEAKNESSES)
            .build();

    @InjectMocks
    private DynamicPokemonTypeModel model;

    @Mock
    private PokemonAPIService pokemonAPIService;

    @Test
    void testInitWeaknessWithValidPokemon() throws NoSuchFieldException {

        PrivateAccessor.setField(model, POKEMON_ID, VALID_POKEMON_ID);
        PrivateAccessor.setField(model, TITLE, WEAKNESS);
        when(pokemonAPIService.getPokemon(VALID_POKEMON_ID)).thenReturn(DTO);

        model.init();

        assertEquals(MOCK_WEAKNESSES, model.getExperienceFragments(), "Expected weaknesses to match mock data");
    }

    @Test
    void testInitTypeWithValidPokemon() throws NoSuchFieldException {


        PrivateAccessor.setField(model, POKEMON_ID, VALID_POKEMON_ID);
        PrivateAccessor.setField(model, TITLE, TYPE);
        when(pokemonAPIService.getPokemon(VALID_POKEMON_ID)).thenReturn(DTO);

        model.init();

        assertEquals(MOCK_TYPES, model.getExperienceFragments(), "Expected weaknesses to match mock data");
    }

    @Test
    void testInitWithLessAllowedPokemonId() throws NoSuchFieldException {

        PrivateAccessor.setField(model, POKEMON_ID, POKEMON_ID_LESS_ALLOWED);
        PrivateAccessor.setField(model, TITLE, WEAKNESS);

        model.init();

        assertNull(model.getExperienceFragments(), WEAKNESS_NULL);
    }

    @Test
    void testInitWithMaxAllowedPokemonId() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID, POKEMON_ID_MAX_ALLOWED);
        PrivateAccessor.setField(model, TITLE, WEAKNESS);

        model.init();

        assertNull(model.getExperienceFragments(), WEAKNESS_NULL);
    }

    @Test
    void testInitWithNullTitle() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID, VALID_POKEMON_ID);
        PrivateAccessor.setField(model, TITLE, null);

        model.init();

        assertNull(model.getExperienceFragments(), WEAKNESS_NULL);
    }

    @Test
    void testInitWithPokemonAPIServiceNull() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID, VALID_POKEMON_ID);
        PrivateAccessor.setField(model, "pokemonAPIService", null);

        model.init();

        assertNull(model.getExperienceFragments(), WEAKNESS_NULL);
    }
}
