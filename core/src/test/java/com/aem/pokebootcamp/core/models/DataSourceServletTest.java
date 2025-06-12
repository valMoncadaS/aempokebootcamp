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

import static junit.framework.Assert.*;

@ExtendWith(AemContextExtension.class)
class DataSourceServletTest {

    AemContext aemContext = new AemContext();
    DataSourceServlet servlet = new DataSourceServlet();
    private Object dataSourceServlet;

    @BeforeEach
    void setUp() {
        aemContext.load().json("/components/PokemonCardModel/PokemonTag.json", "/content");
    }

    @Test
    void doGet() throws IOException {
        Resource resource = aemContext.resourceResolver().getResource("/content/pokemoncards");
        assertNotNull(resource);
        servlet.doGet(aemContext.request(), aemContext.response());
        DataSource dataSourceServlet = (DataSource) aemContext.request().getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceServlet);

        ArrayList<ArrayList<String>> expectedOutputs = new ArrayList<>();
        ArrayList<String> normalOutput = new ArrayList<>();
        ArrayList<String> grassOutput = new ArrayList<>();

        normalOutput.add("Normal");
        normalOutput.add("pokemontags:normal");
        grassOutput.add("Grass");
        grassOutput.add("pokemontags:grass");

        expectedOutputs.add(normalOutput);
        expectedOutputs.add(grassOutput);

        ArrayList<ArrayList<String>> dataSourceOutputs = new ArrayList<>();
        Iterator<Resource> elements = dataSourceServlet.iterator();
        while (elements.hasNext()) {
            Resource resource1 = elements.next();
            ArrayList<String> temp = new ArrayList<>();
            temp.add(resource1.getValueMap().get("text", String.class));
            temp.add(resource1.getValueMap().get("value", String.class));
            dataSourceOutputs.add(temp);
        }

        assertEquals(expectedOutputs, dataSourceOutputs);
    }
}