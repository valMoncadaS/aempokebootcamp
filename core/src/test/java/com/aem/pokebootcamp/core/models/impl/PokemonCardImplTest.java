package com.aem.pokebootcamp.core.models.impl;

import com.aem.pokebootcamp.core.models.PokemonCard;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PokemonCardImplTest {

    private final AemContext aemContext = new AemContext();
    private PokemonCard pokemonCard;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(PokemonCardImpl.class);
        aemContext.load().json("src/test/java/com/aem/pokebootcamp/core/resources/models/impl/PokemonCardTest.json", "/content");
        aemContext.currentResource("/content/pokemoncard");

        pokemonCard = aemContext.request().adaptTo(PokemonCard.class);
    }

    @Test
    void getPokemonTypes() {

        final List<String> expectedTypes = Arrays.asList("bug", "poison");

        assertEquals(expectedTypes, pokemonCard.getPokemonTypes());
    }

    @Test
    void getName() {
    }

    @Test
    void getFileReference() {
    }
}