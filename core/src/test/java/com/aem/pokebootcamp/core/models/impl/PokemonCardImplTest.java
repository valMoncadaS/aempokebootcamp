package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCard;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PokemonCardImplTest {

    private final AemContext aemContext = new AemContext();
    private Resource resource;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCard.class);
        aemContext.load().json("src/test/resources/com/aem/pokebootcamp/core/models/impl/PokemonCardTest.json", "/content");
        aemContext.currentResource("/content/pokemoncard");
    }

    @Test
    void getPokemonTypes() {
        resource = aemContext.resourceResolver().getResource("/content/pokemoncard");
        PokemonCard pokemonCard = aemContext.getService(ModelFactory.class).createModel(resource, PokemonCard.class);

        final List<String> expectedTypes = Arrays.asList("bug", "poison");

        assertEquals(expectedTypes, pokemonCard.getPokemonTypes());
    }

    @Test
    void getName() {
        resource = aemContext.resourceResolver().getResource("/content/pokemoncard");
        PokemonCard pokemonCard = aemContext.getService(ModelFactory.class).createModel(resource, PokemonCard.class);

        final String expectedName = "Weedle";

        assertEquals(expectedName, pokemonCard.getName());
    }

    @Test
    void getFileReference() {
        resource = aemContext.resourceResolver().getResource("/content/pokemoncard");
        PokemonCard pokemonCard = aemContext.getService(ModelFactory.class).createModel(resource, PokemonCard.class);

        final String expectedFileReference = "/content/dam/aempokebootcamp/pokemons/weedle.png";

        assertEquals(expectedFileReference, pokemonCard.getFileReference());
    }
}