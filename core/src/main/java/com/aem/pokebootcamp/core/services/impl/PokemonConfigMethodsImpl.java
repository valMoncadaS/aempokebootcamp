package com.aem.pokebootcamp.core.services.impl;

import com.aem.pokebootcamp.core.config.PokemonConfig;
import com.aem.pokebootcamp.core.services.PokemonConfigMethods;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

/**
 * The {@code PokemonConfigMethodsImpl} class is an OSGi component that implements
 * the {@code PokemonConfigMethods} interface. This class retrieves configuration
 * data from the {@code PokemonConfig} interface and makes it accessible through
 * its methods.
 * The component is configured and activated using OSGi annotations, and it retrieves
 * the URL property from the configuration during activation.
 * Annotations:
 * - {@code @Component}: Declares this class as an OSGi component and registers it
 *   as a service implementing the {@code PokemonConfigMethods} interface.
 * - {@code @Designate}: Associates the component with the {@code PokemonConfig} interface,
 *   which defines the configuration metadata.
 * Key Responsibilities:
 * - Retrieve and store the URL property from the configuration during activation.
 * - Provide the URL value through the method defined in the {@code PokemonConfigMethods}
 *   interface.
 * This class ensures the URL configuration property is properly initialized and accessible
 * during the component's lifecycle within the OSGi runtime.
 */
@Component(service = PokemonConfigMethods.class)
@Designate(ocd = PokemonConfig.class)
public class PokemonConfigMethodsImpl implements PokemonConfigMethods {

    private String url;

    /**
     * Activates the component and initializes its configuration by retrieving the URL
     * parameter from the provided {@code PokemonConfig}.
     * This method is automatically invoked during the component activation phase.
     *
     * @param config an instance of {@code PokemonConfig} containing the configuration
     *               properties, including the URL to be used by the component
     */
    @Activate
    protected void activate(final PokemonConfig config) {
        url = config.url();
    }

    /**
     * Retrieves the URL stored in the component configuration.
     *
     * @return the URL as a string
     */
    @Override
    public String getUrl() {
        return url;
    }
}
