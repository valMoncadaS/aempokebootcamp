package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.day.cq.tagging.Tag;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Resource resource = ctx.resourceResolver().getResource("/content/content/pokemoncards");
        assertNotNull(resource);

        ctx.create().resource("/content/cq:tags/types/poison", "jcr:primaryType", "cq:Tag");
        ctx.create().resource("/content/cq:tags/types/fire", "jcr:primaryType", "cq:Tag");

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        ctx.registerAdapter(Resource.class, Tag.class, (Function<Resource, Tag>) (res) -> {
            Tag mockTag = mock(Tag.class);
            String name = res.getName();

            when(mockTag.getTagID()).thenReturn(res.getPath());
            when(mockTag.getTitle()).thenReturn(name);

            return mockTag;
        });

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceObj);
        assertInstanceOf(DataSource.class, dataSourceObj);

        DataSource dataSource = (DataSource) dataSourceObj;
        Iterator<Resource> items = dataSource.iterator();
        int count = 0;

        while (items.hasNext()) {
            Resource res = items.next();
            ValueMap valueMap = res.getValueMap();
            String value = valueMap.get("value", String.class);
            String text = valueMap.get("text", String.class);

            assertNotNull(value);
            assertNotNull(text);
            assertTrue(value.equals("poison") || value.equals("fire"));
            assertTrue(text.equals("poison") || text.equals("fire"));

            count++;
        }

        assertEquals(2, count);
    }

    @Test
    void doGetTagsPathFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/content/pokemoncards-noTagsPath");
        assertNotNull(resource);

        ctx.create().resource("/content/cq:tags/types/poison", "jcr:primaryType", "cq:Tag");
        ctx.create().resource("/content/cq:tags/types/fire", "jcr:primaryType", "cq:Tag");

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNull(dataSourceObj);
    }

    @Test
    void doGetTagsResourceFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/content/pokemoncards");
        assertNotNull(resource);

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNull(dataSourceObj);
    }

    @Test
    void doGetSkipAdaptTagFailsTest() {
        Resource resource = ctx.resourceResolver().getResource("/content/content/pokemoncards");
        assertNotNull(resource);

        ctx.create().resource("/content/cq:tags/types/poison", "jcr:primaryType", "");

        ctx.currentResource(resource);
        SlingHttpServletRequest request = ctx.request();

        ResourceResolver resourceResolver = mock(ResourceResolver.class);
        when(resourceResolver.adaptTo(Tag.class)).thenReturn(null);

        tagDropdownServlet.doGet(request, ctx.response());

        Object dataSourceObj = request.getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceObj);
        assertInstanceOf(DataSource.class, dataSourceObj);

        DataSource dataSource = (DataSource) dataSourceObj;
        Iterator<Resource> items = dataSource.iterator();

        int totalTags = 0;
        if (items.hasNext()) {
            totalTags++;
        }

        assertEquals(0, totalTags);
    }
}