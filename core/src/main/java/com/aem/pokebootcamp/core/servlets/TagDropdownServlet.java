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

/**
 * TagDropddownServlet to create dynamic dropdown based on tags.
 */
@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "aem/pokebootcamp/tagDropdown",
        methods = HttpConstants.METHOD_GET
)
public class TagDropdownServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected final void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {

        final Resource pathResource = request.getResource();
        final List<Resource> resourceList = new ArrayList<>();
        final ResourceResolver resourceResolver = request.getResourceResolver();

        final String tagsPath = Objects.requireNonNull(
                        pathResource.getChild("datasource"))
                .getValueMap()
                .get("tagsPath", String.class);
        if (tagsPath == null) {
            log.error("No tagsPath found at {} datasource", pathResource);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        final Resource tagsResource = resourceResolver.getResource(tagsPath);
        if (tagsResource == null) {
            log.error("tagsResource not found {}", tagsPath);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            for (final Resource childTags : tagsResource.getChildren()) {
                final ValueMap valueMap = new ValueMapDecorator(new HashMap<>());

                final Tag pokemonTypes = childTags.adaptTo(Tag.class);
                if (pokemonTypes == null) {
                    log.warn("Unable to adapt to Tag class {}", childTags);
                    continue;
                }

                final String tagFullName = pokemonTypes.getTagID();
                final String tagName = tagFullName.substring(tagFullName.lastIndexOf('/') + 1);
                final String tagTitle = pokemonTypes.getTitle();

                valueMap.put("value", tagName);
                valueMap.put("text", tagTitle);

                resourceList.add(
                        new ValueMapResource(
                                resourceResolver,
                                new ResourceMetadata(),
                                "nt:unstructured", valueMap
                        )
                );
            }

            final DataSource dataSource = new SimpleDataSource(resourceList.iterator());
            request.setAttribute(DataSource.class.getName(), dataSource);

            log.info("Tags successfully exported using datasource");
        }
    }
}
