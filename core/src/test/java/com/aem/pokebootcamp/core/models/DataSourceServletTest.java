package com.aem.pokebootcamp.core.models;
import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Data Source to validate the information is working as expected.
 */
@ExtendWith(AemContextExtension.class)
class DataSourceServletTest {

    private final AemContext aemContext = new AemContext();
    private final DataSourceServlet servlet = new DataSourceServlet();

    @BeforeEach
    void setUp() {
        aemContext.load().json("/components/PokemonCardModel/PokemonTag.json", "/content");
    }

    @Test
    void doGet() throws IOException {
        final Resource resource = aemContext.resourceResolver().getResource("/content/pokemoncards");
        aemContext.currentResource(resource);
        assertNotNull(resource, "The resource was found");
        servlet.doGet(aemContext.request(), aemContext.response());
        final DataSource dataSourceServlet = (DataSource) aemContext.request().getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceServlet, "The info requested was found");

        final List<List<String>> expectedOutputs = new ArrayList<>();
        final List<String> normalOutput = new ArrayList<>();
        final List<String> grassOutput = new ArrayList<>();

        normalOutput.add("Normal");
        normalOutput.add("pokemontags:normal");
        grassOutput.add("Grass");
        grassOutput.add("pokemontags:grass");

        expectedOutputs.add(normalOutput);
        expectedOutputs.add(grassOutput);

        final List<List<String>> dataSourceOutputs = new ArrayList<>();
        final Iterator<Resource> elements = dataSourceServlet.iterator();
        while (elements.hasNext()) {
            final Resource resource1 = elements.next();
            final List<String> temp = new ArrayList<>();
            temp.add(resource1.getValueMap().get("text", String.class));
            temp.add(resource1.getValueMap().get("value", String.class));
            dataSourceOutputs.add(temp);
        }

        assertEquals(expectedOutputs, dataSourceOutputs, "Both tags match");
    }
}