package com.aem.pokebootcamp.core.models;
import com.adobe.granite.ui.components.ds.DataSource;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(AemContextExtension.class)
class DataSourceServletTest {

    AemContext aemContext = new AemContext();
    DataSourceServlet servlet = new DataSourceServlet();
    private Object dataSourceServlet;

    @Test
    void doGet() throws IOException {
        TagManager tagManagerMock = mock(TagManager.class);
        Tag rootTag = mock(Tag.class);
        when(tagManagerMock.resolve("/content/cq:tags/pokemontags")).thenReturn(rootTag);

        Tag tag1 = mock(Tag.class);
        Tag tag2 = mock(Tag.class);
        Iterator<Tag> tagIteratorMock = Arrays.asList(tag1, tag2).iterator();
        when(rootTag.listAllSubTags()).thenReturn(tagIteratorMock);

        when(tag1.getTagID()).thenReturn("pokemontags:normal");
        when(tag2.getTagID()).thenReturn("pokemontags:grass");
        when(tag1.getTitle()).thenReturn("Normal");
        when(tag2.getTitle()).thenReturn("Grass");
        when(tag1.getPath()).thenReturn("/content/cq:tags/pokemontags/normal");
        when(tag2.getPath()).thenReturn("/content/cq:tags/pokemontags/grass");

        aemContext.registerAdapter(ResourceResolver.class, TagManager.class , tagManagerMock);
        aemContext.registerInjectActivateService(servlet);
        servlet.doGet(aemContext.request(), aemContext.response());
        dataSourceServlet = aemContext.request().getAttribute(DataSource.class.getName());
        assertNotNull(dataSourceServlet);
    }
}