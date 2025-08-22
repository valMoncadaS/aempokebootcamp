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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing for Pokémon Details Model to see if the Pokémon weight, height, gender, ability and category
 * matches with Json.
 */
@ExtendWith(AemContextExtension.class)
class PokemonDetailsModelTest {

    private final AemContext aemContext = new AemContext();
    private PokemonDetailsModel pokemonDetailsModel;

    /**
     * Load the json to the context for testing it with the code (Expected Vs Current).
     */
    @SuppressWarnings("PMD.CloseResource")
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonDetailsModel.class);
        aemContext.load().json("/components/PokemonCardModel/PokemonDetails.json", "/component");

        final ResourceResolver resourceR = aemContext.resourceResolver();
        final Resource rsc = resourceR.getResource("/component/pokemondetails");
        pokemonDetailsModel = aemContext.getService(ModelFactory.class).createModel(rsc, PokemonDetailsModel.class);
    }

    @Test
    void testStatistics() {
        final Double expectedHP = 0.7;
        final Double actualHP = pokemonDetailsModel.getHeight();
        assertEquals(expectedHP, actualHP, "Expected HP and Actual HP are equal!");

        final Double expectedWeight = 6.9;
        final Double actualWeight = pokemonDetailsModel.getWeight();
        assertEquals(expectedWeight, actualWeight, "Expected Attack and Actual Attack are equal!");

        final String expectedAbility = "";
        final String actualAbility = pokemonDetailsModel.getAbility();
        assertEquals(expectedAbility, actualAbility, "Expected Ability and Actual Ability are equal!");

        final String expectedCategory = "";
        final String actualCategory = pokemonDetailsModel.getCategory();
        assertEquals(expectedCategory, actualCategory,
                "Expected Category and Actual Category are equal!");

        final Boolean selectedFemaleGender = pokemonDetailsModel.getFemaleGender();
        assertTrue(selectedFemaleGender, "Female gender is selected (TRUE)");

        final Boolean selectedMaleGender = pokemonDetailsModel.getMaleGender();
        assertTrue(selectedMaleGender, "Male gender is selected (TRUE)");
    }
}
