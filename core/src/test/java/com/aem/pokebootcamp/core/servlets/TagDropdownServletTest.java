package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TagDropdownServlet}.
 * <p>
 * It verifies that:
 * - The servlet correctly populates the DataSource when valid tag paths are present.
 * - It returns null or error status when tag-related resources are missing or invalid.
 * <p>
 * Test scenarios covered:
 * 1. Successful population of dropdown with tag data.
 * 2. Failure when 'cq:tagsPath' is not provided.
 * 3. Failure when 'cq:tagsPath' points to a non-existing resource.
 * 4. Filtering logic works (i.e., non-adaptable tags are skipped).
 */
@ExtendWith(AemContextExtension.class)
class TagDropdownServletTest {
    private final AemContext ctx = new AemContext();
    private TagDropdownServlet tagDropdownServlet;

    @BeforeEach
    void setUp() {
        ctx.load().json("/com/aem/pokebootcamp/core/servlets/TagDropdownServletTest.json",
                "/content");
        tagDropdownServlet = new TagDropdownServlet();
    }


    @Test
    void doGetSuccessfulTest() throws IOException {
        final String text = "text";
        final String value = "value";

        final Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards");

        ctx.currentResource(resource);
        final SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        final Object dataSourceObj = request.getAttribute(DataSource.class.getName());

        final DataSource dataSource = (DataSource) dataSourceObj;
        final Iterator<Resource> items = dataSource.iterator();
        final List<Map<String, String>> resourceList = IteratorUtils
                .toList(items)
                .stream()
                .map(Resource::getValueMap)
                .map(valueMap -> Map.of(
                        text, valueMap.get(text, String.class),
                        value, valueMap.get(value, String.class)))
                .collect(Collectors.toList());

        assertEquals(List.of(
                        Map.of(text, "Poison",
                                value, "types:poison"),
                        Map.of(text, "Fire",
                                value, "types:fire")),
                resourceList,
                "Resource list mismatch: expected entries for 'Poison' and 'Fire' types");
    }

    @Test
    void doGetTagsPathFailsTest() throws IOException {
        final Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards-noTagsPath");

        ctx.currentResource(resource);
        final SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        final Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNull(dataSourceObj, "Expected no DataSource to be set when 'cq:tagsPath' is missing");
    }

    @Test
    void doGetTagsResourceFailsTest() throws IOException {
        final Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards-wrongTagsPath");

        ctx.currentResource(resource);
        final SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        assertEquals(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, ctx.response().getStatus(),
                "Response status should be 500 when 'cq:tagsPath' points to a non-existent resource");
    }

    @Test
    void doGetSkipAdaptTagFailsTest() throws IOException {
        final Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards");

        ctx.currentResource(resource);
        final SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        final Object dataSourceObj = request.getAttribute(DataSource.class.getName());

        final DataSource dataSource = (DataSource) dataSourceObj;
        final List<Resource> resourceList = IteratorUtils.toList(dataSource.iterator());

        assertEquals(2, resourceList.size(), "Expected exactly 2 resources in the list");
    }
}