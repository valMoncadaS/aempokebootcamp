package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class TagDropdownServletTest {
    private final AemContext ctx = new AemContext();
    TagDropdownServlet tagDropdownServlet;

    @BeforeEach
    void setUp() {
        ctx.load().json("/com/aem/pokebootcamp/core/servlets/TagDropdownServletTest.json", "/content");
        tagDropdownServlet = new TagDropdownServlet();
    }


    @Test
    void doGetSuccessfulTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards");
        assertNotNull(resource);

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceObj);
        assertInstanceOf(DataSource.class, dataSourceObj);

        DataSource dataSource = (DataSource) dataSourceObj;
        Iterator<Resource> items = dataSource.iterator();
        List<Map<String, String>> resourceList = IteratorUtils
                .toList(items)
                .stream()
                .map(Resource::getValueMap)
                .map(valueMap -> Map.of(
                        "text", valueMap.get("text", String.class),
                        "value", valueMap.get("value", String.class)))
                .collect(Collectors.toList());

        assertEquals(List.of(
                        Map.of(
                                "text", "Poison",
                                "value", "types:poison"),
                        Map.of(
                                "text", "Fire",
                                "value", "types:fire")),
                resourceList);

    }

    @Test
    void doGetTagsPathFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards-noTagsPath");
        assertNotNull(resource);

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNull(dataSourceObj);
    }

    @Test
    void doGetTagsResourceFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards-wrongTagsPath");
        assertNotNull(resource);

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNull(dataSourceObj);
    }

    @Test
    void doGetSkipAdaptTagFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/pokemoncards");
        assertNotNull(resource);

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceObj);
        assertInstanceOf(DataSource.class, dataSourceObj);

        DataSource dataSource = (DataSource) dataSourceObj;
        List<Resource> resourceList = IteratorUtils.toList(dataSource.iterator());

        assertEquals(2, resourceList.size());
    }
}