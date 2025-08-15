package com.aem.pokebootcamp.core.models;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing for Pokémon Stats Model to see if hp, attack, special attack, defense, special defense and speed
 * matches with Json.
 */
@ExtendWith(AemContextExtension.class)
class PokemonStatsModelTest {

    private final AemContext aemContext = new AemContext();
    private PokemonStatsModel pokemonStatsModel;

    /**
     * Load the json to the context for testing it with the code (Expected Vs Current).
     */
    @SuppressWarnings("PMD.CloseResource")
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonStatsModel.class);
        aemContext.load().json("/components/PokemonCardModel/PokemonStats.json", "/component");

        final ResourceResolver resourceR = aemContext.resourceResolver();
        final Resource rsc = resourceR.getResource("/component/pokemonstats");
        pokemonStatsModel = aemContext.getService(ModelFactory.class).createModel(rsc, PokemonStatsModel.class);
    }

    @Test
    void testStatistics() {
        final Long expectedHP = 20L;
        final Long actualHP = pokemonStatsModel.getHitPoints();
        assertEquals(expectedHP, actualHP, "Expected HP and Actual HP are equal!");

        final Long expectedAttack = 240L;
        final Long actualAttack = pokemonStatsModel.getAttack();
        assertEquals(expectedAttack, actualAttack, "Expected Attack and Actual Attack are equal!");

        final Long expectedDefense = 100L;
        final Long actualDefense = pokemonStatsModel.getDefense();
        assertEquals(expectedDefense, actualDefense, "Expected Defense and Actual Defense are equal!");

        final Long expectedSpecialAttack = 60L;
        final Long actualSpecialAttack = pokemonStatsModel.getSpecialAttack();
        assertEquals(expectedSpecialAttack, actualSpecialAttack,
            "Expected Special Attack and Actual Special Attack are equal!");

        final Long expectedSpecialDefense = 197L;
        final Long actualSpecialDefense = pokemonStatsModel.getSpecialDefense();
        assertEquals(expectedSpecialDefense, actualSpecialDefense,
            "Expected Special Defense and Actual Special Defense are equal!");

        final Long expectedSpeed = 210L;
        final Long actualSpeed = pokemonStatsModel.getSpeed();
        assertEquals(expectedSpeed, actualSpeed, "Expected Speed and Actual Speed are equal!");
    }
}
