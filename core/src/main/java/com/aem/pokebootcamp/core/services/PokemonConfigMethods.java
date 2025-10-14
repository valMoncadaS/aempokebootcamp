package com.aem.pokebootcamp.core.services;

/**
 * The {@code PokemonConfigMethods} interface defines a contract for retrieving
 * configuration data related to a URL. Implementations of this interface are
 * expected to provide the ability to access the URL configured in a related
 * OSGi configuration.
 * Primary Responsibilities:
 * - Define a method to retrieve the URL from the configuration.
 * - Serve as the base interface for classes responsible for interacting with
 *   OSGi configurations providing URL details.
 */
public interface PokemonConfigMethods {
    /**
     * Retrieves the URL configured in the associated OSGi configuration.
     *
     * @return the URL as a String, or null if the URL is not configured.
     */
    String getUrl();
}
