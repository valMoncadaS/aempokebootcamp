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
 * Testing for Pokémon Type Model to see if title matches with Json.
 */

@ExtendWith(AemContextExtension.class)
class PokemonTypeModelTest {

    private final AemContext aemContext = new AemContext();
    private PokemonTypeModel pokemonTypeModel;

    /**
     * Load the json to the context for testing it with the code (Expected Vs Current).
     */
    @SuppressWarnings("PMD.CloseResource")
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCardModel.class);
        aemContext.load().json("/components/PokemonCardModel/PokemonType.json", "/component");

        final ResourceResolver resourceR = aemContext.resourceResolver();
        final Resource rsc = resourceR.getResource("/component/pokemontype");
        pokemonTypeModel = aemContext.getService(ModelFactory.class).createModel(rsc, PokemonTypeModel.class);
    }

    @Test
    void testType() {
        final String expectedTitle = "Weakness";
        final String actualTitle = pokemonTypeModel.getTitle();
        assertEquals(expectedTitle, actualTitle, "expected Title and actual Title are equal!");
    }
}
