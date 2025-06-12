package com.aem.pokebootcamp.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
public class PokemonCardModelTest {

    AemContext aemContext = new AemContext();
    PokemonCardModel pokemonCardModel;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCardModel.class);
        aemContext.load().json("/components/PokemonCardModel/PokemonCardModel.json", "/component");

        Resource resource = aemContext.resourceResolver().getResource("/component/data/jcr:content/root/container/container/pokemoncards");
        pokemonCardModel = aemContext.getService(ModelFactory.class).createModel(resource, PokemonCardModel.class);
    }

    @Test
    void testGetMultifieldList() {
        String expectedFileReference = "/content/dam/aempokebootcamp/bulbasaur.png";
        String actualFileReference = pokemonCardModel.getMultifieldList().get(0).getFileReference();
        assertEquals(expectedFileReference, actualFileReference);

        String expectedName = "Bulbasaur";
        String actualName = pokemonCardModel.getMultifieldList().get(0).getName();
        assertEquals(expectedName, actualName);
    }
}
