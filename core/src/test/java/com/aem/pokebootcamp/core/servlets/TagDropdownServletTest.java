package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TagDropdownServlet class.
 *
 * These tests validate the behavior of methods for class TagDropdownServlet
 * under various scenarios, including valid and invalid input data, missing configurations,
 * and empty tag collections. The tests ensure that the servlet correctly processes the data
 * and sets the appropriate DataSource for dropdown rendering in AEM.
 */
@ExtendWith(AemContextExtension.class)
class TagDropdownServletTest {
    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    private TagDropdownServlet servlet;

    @BeforeEach
    void setUp() {
        context.load().json("/com/aem/pokebootcamp/core/servlets/TagDropdownServletTest.json",
                "/content");
        servlet = new TagDropdownServlet();
    }

    @Test
    void doGetWithValidTagsPath() throws IOException {
        final String title = "text";
        final String value = "value";

        final Resource pokemonTypesResource = context.resourceResolver().getResource("/content/pokemon-types");

        context.currentResource(pokemonTypesResource);

        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();
        servlet.doGet(request, response);

        final DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());
        assertNotNull(dataSource, "Expected DataSource to be set when 'cq:tagsPath' is correct");

        final Iterator<Resource> tags = dataSource.iterator();
        final List<Map<String, String>> tagsList = IteratorUtils
                .toList(tags)
                .stream()
                .map(Resource::getValueMap)
                .map(valueMap -> Map.of(
                        title, Objects.requireNonNull(valueMap.get(title, String.class)),
                        value, Objects.requireNonNull(valueMap.get(value, String.class))))
                .collect(Collectors.toList());

        assertEquals(List.of(
                        Map.of(title, "Bug",
                                value, "pokemon-types:bug"),
                        Map.of(title, "Dragon",
                                value, "pokemon-types:dragon"),
                        Map.of(title, "Electric",
                                value, "pokemon-types:electric")),
                tagsList,
                "Tag list expected error: Is expected a list with correct pokemon types "
                + "(Bug, Dragon, Electric)");
    }

    @Test
    void doGetWithoutTagsPath() throws IOException {
        final Resource pokemonTypesResource = context.resourceResolver()
                .getResource("/content/pokemon-types-no-tagsPath");

        context.currentResource(pokemonTypesResource);

        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();
        servlet.doGet(request, response);

        final DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());
        assertNull(dataSource, "Expected no DataSource to be set when 'cq:tagsPath' is missing");
    }

    @Test
    void doGetWithInvalidTagsPath() throws IOException {
        final Resource pokemonTypesResource = context.resourceResolver()
                .getResource("/content/pokemon-types-invalid-tagsPath");

        context.currentResource(pokemonTypesResource);

        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();
        servlet.doGet(request, response);

        final DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());
        assertNull(dataSource, "Expected no DataSource to be set when 'cq:tagsPath' is incorrect");
    }

    @Test
    void doGetWithEmptyTagsCollection() throws IOException {
        final Resource pokemonTypesResource = context.resourceResolver()
                .getResource("/content/pokemon-types-empty-list");

        context.currentResource(pokemonTypesResource);

        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();
        servlet.doGet(request, response);
        final DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());

        assertNotNull(dataSource, "Expected DataSource to be set when 'cq:tagsPath' is correct");

        final Iterator<Resource> tags = dataSource.iterator();
        assertFalse(tags.hasNext(), "Expected no tags in the DataSource when the tag collection is empty");
    }

    @Test
    void doGetWithNullTagsCollection() throws IOException {
        final Resource pokemonTypesResource = context.resourceResolver()
                .getResource("/content/pokemon-types-null-tag");
        context.currentResource(pokemonTypesResource);

        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();

        servlet.doGet(request, response);
        final DataSource dataSource = (DataSource) request.getAttribute(DataSource.class.getName());

        assertNotNull(dataSource, "Expected DataSource to be set when 'cq:tagsPath' is correct");

        final Iterator<Resource> tags = dataSource.iterator();
        assertFalse(tags.hasNext(), "Expected no tags in the DataSource when the tags are null");
    }
}
