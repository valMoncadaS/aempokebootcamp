package com.aem.pokebootcamp.core.models;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.tagging.Tag;
import lombok.extern.slf4j.Slf4j;
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
import java.io.Serializable;
import java.util.*;

/**
 * Data Source created to get all Pokémon tags.
 */
@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = DataSourceServlet.RESOURCE_TYPE,
        methods = HttpConstants.METHOD_GET
)
public class DataSourceServlet extends SlingSafeMethodsServlet implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * DataSource with all tags coming from cq:tags/pokemontags.
     */
    protected static final String RESOURCE_TYPE = "apps/aempokebootcamp/components/pokemoncomponents/pokemonCards";
    /**
     * Function that adds those Pokémon Tags (Tag id:Tag Title) to the DataSource.
     */
    @SuppressWarnings("PMD.CloseResource")
    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws IOException {
        final ResourceResolver resolver = request.getResourceResolver();
        final Resource pathResource = request.getResource();
        final Resource dataSourcePathR = Objects.requireNonNull(pathResource.getChild("datasource"));
        final String tagsPath = dataSourcePathR.getValueMap().get("tagsPath", String.class);
        if (tagsPath != null) {
            final Resource tagsResource = request.getResourceResolver().getResource(tagsPath);
            final List<Resource> resources = new ArrayList<>();
            if (tagsResource != null) {
                for (final Resource childTag : tagsResource.getChildren()) {
                    final Tag tag = childTag.adaptTo(Tag.class);
                    final ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
                    valueMap.put("value", tag.getTagID());
                    valueMap.put("text", tag.getTitle());
                    resources.add(new ValueMapResource(resolver, tag.getPath(), "nt:unstructured", valueMap));
                }
                final DataSource dataSource = new SimpleDataSource(resources.iterator());
                request.setAttribute(DataSource.class.getName(), dataSource);
                log.info("tags successfully exported!");
            } else {
                log.warn("tagsResource is empty!");
            }
        } else {
            log.warn("tagsPath not found!");
        }
    }
}
