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
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Servlet for dynamically populating a dropdown with tag values.
 * It fetches a list of tags from a specified path, processes them, and provides
 * them in a format suitable for rendering as a dropdown in a client-side component.
 *
 * This servlet is specifically designed to handle GET requests and works with resources
 * of type "aempokebootcamp/tagdropdown".
 */
@Slf4j
@SuppressWarnings("PMD.CloseResource")
@Component(service = Servlet.class, property = {Constants.SERVICE_DESCRIPTION + "= Tags value in dynamic Dropdown"
})
@SlingServletResourceTypes(
        resourceTypes = "aempokebootcamp/tagdropdown",
        methods = HttpConstants.METHOD_GET
)
public class TagDropdownServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Method designed to return a list of tags according provided datasource tagsPath attribute.
     * @param request
     * @param response
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        final Resource pathResource;
        final List<Resource> resourceList;
        final ResourceResolver resourceResolver = request.getResourceResolver();
        ValueMap valueMap;
        pathResource = request.getResource();
        resourceList = new ArrayList<>();

        log.info("Getting tags path from datasource node");
        final String tagsPath = Objects.requireNonNull(pathResource.getChild("datasource")).getValueMap()
                .get("tagsPath", String.class);
        if (tagsPath != null) {
            log.info("Tags path found: {}", tagsPath);
            final Resource tagsResource = request.getResourceResolver().getResource(tagsPath);

            if (tagsResource != null) {
                for (final Resource childTags : tagsResource.getChildren()) {
                    valueMap = new ValueMapDecorator(new HashMap<>());
                    final Tag tag = childTags.adaptTo(Tag.class);
                    if (tag != null) {
                        final String tagFullName = tag.getTagID();
                        final String tagName = tagFullName.substring(tagFullName.lastIndexOf('/') + 1);
                        final String tagTitle = tag.getTitle();
                        valueMap.put("value", tagName);
                        valueMap.put("text", tagTitle);
                        resourceList.add(new ValueMapResource(resourceResolver,
                                new ResourceMetadata(), "nt:unstructured", valueMap));
                    }
                }

                log.info("Creating DataSource for tags");

                final DataSource dataSource = new SimpleDataSource(resourceList.iterator());
                request.setAttribute(DataSource.class.getName(), dataSource);

                log.info("Tags successfully exported using DataSource!");
            }
        } else {
            log.info("No tags path found");
        }
    }
}
