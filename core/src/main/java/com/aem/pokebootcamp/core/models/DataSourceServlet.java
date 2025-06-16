package com.aem.pokebootcamp.core.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "apps/aempokebootcamp/components/pokemoncomponents/pokemonCards",
        methods = HttpConstants.METHOD_GET
)
public class DataSourceServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        ResourceResolver resolver = request.getResourceResolver();
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        Tag rootTag = tagManager.resolve("/content/cq:tags/pokemontags");

        if (rootTag != null) {
            List<Resource> resources = new ArrayList<>();
            Iterator<Tag> tagIterator = rootTag.listAllSubTags();
            while (tagIterator.hasNext()) {
                Tag tag = tagIterator.next();
                ValueMap vm = new ValueMapDecorator(new HashMap<>());
                vm.put("value", tag.getTagID());
                vm.put("text", tag.getTitle());
                resources.add(new ValueMapResource(resolver, tag.getPath(), "nt:unstructured", vm));
            }
            DataSource ds = new SimpleDataSource(resources.iterator());
            request.setAttribute(DataSource.class.getName(), ds);
        }
    }
}
