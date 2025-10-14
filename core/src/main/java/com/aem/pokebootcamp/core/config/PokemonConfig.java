package com.aem.pokebootcamp.core.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.AttributeDefinition;

/**
 * The {@code PokemonConfig} interface serves as an OSGi configuration for defining a URL.
 * It uses the {@code ObjectClassDefinition} annotation for metadata and to enable configuration
 * in the OSGi console.
 * This configuration is meant to provide a single configurable property, the URL, which can
 * be accessed by components using this configuration.
 *
 * @see ObjectClassDefinition
 * @see AttributeDefinition
 */
@ObjectClassDefinition(name = "Pokemon Configuration", description = "Configuration for URL")
public @interface PokemonConfig {

    /**
     * Retrieves the URL configured in the {@code PokemonConfig} interface.
     *
     * @return the configured URL as a string
     */
    @AttributeDefinition(name = "URL", description = "Define a URL")
    String url();
}
