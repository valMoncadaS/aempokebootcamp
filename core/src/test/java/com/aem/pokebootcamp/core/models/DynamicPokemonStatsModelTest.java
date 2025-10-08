package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@code DynamicPokemonStatsModel}, focusing on verifying the initialization
 * and correctness of dynamically populated Pokémon statistics retrieved through the
 * {@code PokemonAPIService}.
 * These tests ensure that the model correctly handles:
 * - Populating Pokémon stats when valid data is returned from the service.
 * - Handling edge cases for Pokémon ID values outside acceptable ranges.
 * - Handling scenarios where the returned stats data is null or incomplete (missing specific keys).
 * The tests use JUnit 5 and Mockito for mocking dependencies and validating behaviors.
 * Test Cases:
 * 1. {@code pokemonStatsPopulation}: Verifies that valid stats are correctly populated in the model.
 * 2. {@code pokemonIdMoreThanAllowed}: Ensures that the model does not populate stats for IDs greater than
 *    the maximum allowed limit.
 * 3. {@code pokemonIdLessThanAllowed}: Ensures that the model does not populate stats for IDs less than
 *    the minimum allowed value.
 * 4. {@code statsNull}: Validates that if the service returns null stats, the model's fields remain null.
 * 5. {@code statsNotContainKey}: Tests cases where specific statistic keys are missing from the returned data.
 */
@ExtendWith(MockitoExtension.class)
class DynamicPokemonStatsModelTest {

    private static final int POKEMON_ID = 25;
    private static final int POKEMON_ID_LESS_ALLOWED = 0;
    private static final int POKEMON_ID_MAX_ALLOWED = 1026;

    private static final String POKEMON_ID_TEXT = "pokemonId";
    private static final String POKEMON_ATTACK_SHOULD_BE_NULL = "Pokemon attack should be null";
    private static final String POKEMON_DEFENSE_SHOULD_BE_NULL = "Pokemon defense should be null";
    private static final String POKEMON_HP_SHOULD_BE_NULL = "Pokemon HP should be null";
    private static final String POKEMON_SPEED_SHOULD_BE_NULL = "Pokemon speed should be null";
    private static final String POKEMON_SPECIAL_ATTACK_SHOULD_BE_NULL = "Pokemon Special Attack should be null";
    private static final String POKEMON_SPECIAL_DEFENSE_SHOULD_BE_NULL = "Pokemon Special Defense should be null";

    private static final Map<String, Integer> POKEMON_STATS = Map.of(
            "hp", 25,
            "attack", 55,
            "defense", 40,
            "special-attack", 50,
            "special-defense", 50,
            "speed", 90);


    @InjectMocks
    private DynamicPokemonStatsModel model;

    @Mock
    private PokemonAPIService pokemonAPIService;

    @Test
    void pokemonStatsPopulation() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID);
        when(pokemonAPIService.getPokemon(POKEMON_ID)).thenReturn(PokemonDTO.builder().stats(POKEMON_STATS).build());
        model.init();

        assertEquals(model.getHealthPoints(), POKEMON_STATS.get("hp").toString(), "Expected Pokemon HP to be '25'");
        assertEquals(model.getAttack(), POKEMON_STATS.get("attack").toString(),
                "Expected Pokemon attack to be '55'");
        assertEquals(model.getDefense(), POKEMON_STATS.get("defense").toString(),
                "Expected Pokemon defense to be '40'");
        assertEquals(model.getSpecialAttack(), POKEMON_STATS.get("special-attack").toString(),
                "Expected Pokemon Special Attack to be '50'");
        assertEquals(model.getSpecialDefense(), POKEMON_STATS.get("special-defense").toString(),
                "Expected Pokemon Special Defense to be '50'");
        assertEquals(model.getSpeed(), POKEMON_STATS.get("speed").toString(),
                "Expected Pokemon speed to be '90'");
    }

    @Test
    void pokemonIdMoreThanAllowed() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID_MAX_ALLOWED);
        model.init();

        assertNull(model.getAttack(), POKEMON_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getDefense(), POKEMON_DEFENSE_SHOULD_BE_NULL);
        assertNull(model.getHealthPoints(), POKEMON_HP_SHOULD_BE_NULL);
        assertNull(model.getSpeed(), POKEMON_SPEED_SHOULD_BE_NULL);
        assertNull(model.getSpecialAttack(), POKEMON_SPECIAL_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getSpecialDefense(), POKEMON_SPECIAL_DEFENSE_SHOULD_BE_NULL);
    }

    @Test
    void pokemonIdLessThanAllowed() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID_LESS_ALLOWED);
        model.init();

        assertNull(model.getAttack(), POKEMON_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getDefense(), POKEMON_DEFENSE_SHOULD_BE_NULL);
        assertNull(model.getHealthPoints(), POKEMON_HP_SHOULD_BE_NULL);
        assertNull(model.getSpeed(), POKEMON_SPEED_SHOULD_BE_NULL);
        assertNull(model.getSpecialAttack(), POKEMON_SPECIAL_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getSpecialDefense(), POKEMON_SPECIAL_DEFENSE_SHOULD_BE_NULL);
    }

    @Test
    void statsNull() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID);
        final PokemonDTO pokemonDTO = PokemonDTO.builder().stats(null).build();
        when(pokemonAPIService.getPokemon(POKEMON_ID)).thenReturn(pokemonDTO);
        model.init();

        assertNull(model.getAttack(), POKEMON_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getDefense(), POKEMON_DEFENSE_SHOULD_BE_NULL);
        assertNull(model.getHealthPoints(), POKEMON_HP_SHOULD_BE_NULL);
        assertNull(model.getSpeed(), POKEMON_SPEED_SHOULD_BE_NULL);
        assertNull(model.getSpecialAttack(), POKEMON_SPECIAL_ATTACK_SHOULD_BE_NULL);
        assertNull(model.getSpecialDefense(), POKEMON_SPECIAL_DEFENSE_SHOULD_BE_NULL);
    }

    @Test
    void statsNotContainKey() throws NoSuchFieldException {
        PrivateAccessor.setField(model, POKEMON_ID_TEXT, POKEMON_ID);
        final Map<String, Integer> stats = new HashMap<>(POKEMON_STATS);

        stats.remove("attack");

        when(pokemonAPIService.getPokemon(POKEMON_ID)).thenReturn(PokemonDTO.builder().stats(stats).build());

        model.init();

        assertNull(model.getAttack(), POKEMON_ATTACK_SHOULD_BE_NULL);
        assertEquals(model.getHealthPoints(), POKEMON_STATS.get("hp").toString(),
                "Expected Pokemon HP to be '25'");
        assertEquals(model.getDefense(), POKEMON_STATS.get("defense").toString(),
                "Expected Pokemon defense to be '40'");
        assertEquals(model.getSpecialAttack(), POKEMON_STATS.get("special-attack").toString(),
                "Expected Pokemon Special Attack to be '50'");
        assertEquals(model.getSpecialDefense(), POKEMON_STATS.get("special-defense").toString(),
                "Expected Pokemon Special Defense to be '50'");
        assertEquals(model.getSpeed(), POKEMON_STATS.get("speed").toString(),
                "Expected Pokemon speed to be '90'");
    }
}
