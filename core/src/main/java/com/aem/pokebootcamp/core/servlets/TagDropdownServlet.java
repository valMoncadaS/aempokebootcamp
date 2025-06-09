package com.aem.pokebootcamp.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.tagging.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.util.*;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "aem/pokebootcamp/tagDropdown",
        methods = HttpConstants.METHOD_GET
)
public class TagDropdownServlet extends SlingSafeMethodsServlet {

    transient ResourceResolver resourceResolver;
    transient Resource pathResource;
    transient ValueMap valueMap;
    transient List<Resource> resourceList;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        resourceResolver = request.getResourceResolver();
        pathResource = request.getResource();
        resourceList = new ArrayList<>();

        String tagsPath = Objects.requireNonNull(pathResource.getChild("datasource")).getValueMap().get("tagsPath", String.class);

        if (tagsPath == null) {
            log.warn("No tagsPath found");
            return;
        }

        Resource tagsResource = resourceResolver.getResource(tagsPath);
        if (tagsResource == null) {
            log.warn("tagsResource not found");
            return;
        }

        for (Resource childTags : tagsResource.getChildren()) {
            valueMap = new ValueMapDecorator(new HashMap<>());

            Tag pokemonTypes = childTags.adaptTo(Tag.class);
            if (pokemonTypes == null) {
                log.info("Unable to adapt to Tag class");
                continue;
            }

            String tagFullName = pokemonTypes.getTagID();
            String tagName = tagFullName.substring(tagFullName.lastIndexOf('/') + 1);
            String tagTitle = pokemonTypes.getTitle();

            valueMap.put("value", tagName);
            valueMap.put("text", tagTitle);

            resourceList.add(new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", valueMap));
        }

        DataSource dataSource = new SimpleDataSource(resourceList.iterator());
        request.setAttribute(DataSource.class.getName(), dataSource);

        log.info("Tags successfully exported using datasource");
    }
}
