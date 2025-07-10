package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class TagDropdownServletTest {

    AemContext aemContext = new AemContext();
    TagDropdownServlet tagDropdownServlet = new TagDropdownServlet();

    @BeforeEach
    void setUp() {
        aemContext.load().json("src/test/resources/com/aem/pokebootcamp/core/servlets/TagDropdownServletTest.json", "/content");
    }

    @Test
    void doGet() {
        final Resource pokemonTypes = aemContext.resourceResolver().getResource("/content/pokemonTypes");
        aemContext.currentResource(pokemonTypes);
        final MockSlingHttpServletRequest request = aemContext.request();
        final MockSlingHttpServletResponse response = aemContext.response();

        tagDropdownServlet.doGet(request, response);

        final DataSource dataSourceObj = (DataSource) request.getAttribute(DataSource.class.getName());

        final String test = response.getOutputAsString();
    }
}