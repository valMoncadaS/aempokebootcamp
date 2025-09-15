package com.aem.pokebootcamp.core.services.impl;

import com.aem.pokebootcamp.core.config.PokemonConfig;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The {@code PokemonConfigMethodsImplTest} class is a unit test for the {@code PokemonConfigMethodsImpl}
 * implementation. This test class ensures that the {@code PokemonConfigMethodsImpl} behaves as expected
 * by verifying its interaction with the OSGi configuration and its ability to retrieve the configured URL.
 * Key Responsibilities:
 * - Sets up an AEM context to simulate OSGi behaviour.
 * - Tests that the {@code activate} method initializes the URL correctly from the {@code PokemonConfig}.
 * - Tests the functionality of the {@code getUrl} method of the {@code PokemonConfigMethodsImpl}.
 * Features:
 * - Uses the {@code AemContext} from the `io.wcm.testing.mock.aem.junit5` library to simulate the OSGi environment.
 */
@ExtendWith(AemContextExtension.class)
class PokemonConfigMethodsImplTest {

    private final AemContext context = new AemContext();

    @Test
    void testActivateWithAemContext() {
        context.registerService(PokemonConfig.class);

        final PokemonConfigMethodsImpl service = context.registerInjectActivateService(new PokemonConfigMethodsImpl(),
                "url", "https://www.pokemon.com/us/pokedex");

        assertEquals("https://www.pokemon.com/us/pokedex", service.getUrl(),
                "Expected Pokemon API URL to be 'https://www.pokemon.com/us/pokedex'");
    }

}
