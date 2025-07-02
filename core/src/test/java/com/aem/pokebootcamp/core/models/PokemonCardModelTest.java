package com.aem.pokebootcamp.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Testing for Pokémon Card Model to see if name and file reference matches with Json.
 */
@ExtendWith(AemContextExtension.class)
class PokemonCardModelTest {

    private final AemContext aemContext = new AemContext();
    private PokemonCardModel pokemonCardModel;

    /**
     * Load the json to the context for testing it with the code (Expected Vs Current).
     */
    @SuppressWarnings("PMD.CloseResource")
    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCardModel.class);
        aemContext.load().json("/components/PokemonCardModel/PokemonCardModel.json", "/component");

        final ResourceResolver resourceR = aemContext.resourceResolver();
        final Resource rsc = resourceR.getResource("/component/data/jcr:content/root/container/container/pokemoncards");
        pokemonCardModel = aemContext.getService(ModelFactory.class).createModel(rsc, PokemonCardModel.class);
    }

    @Test
    void testGetMultifieldList() {
        final String expectedFileReference = "/content/dam/aempokebootcamp/bulbasaur.png";
        final String actualFileReference = pokemonCardModel.getMultifieldList().get(0).getFileReference();
        assertEquals(expectedFileReference, actualFileReference, "Both File References are equal!");

        final String expectedName = "Bulbasaur";
        final String actualName = pokemonCardModel.getMultifieldList().get(0).getName();
        assertEquals(expectedName, actualName, "Both names are equal!");
    }
}
