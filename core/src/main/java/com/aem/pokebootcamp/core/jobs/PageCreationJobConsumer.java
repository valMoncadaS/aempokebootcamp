package com.aem.pokebootcamp.core.jobs;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The PageCreationJobConsumer class is responsible for consuming and processing
 * jobs defined under the "com/aembootcamp/pagecreation" topic. This class handles
 * the creation of pages in Adobe Experience Manager (AEM) based on the properties
 * provided in the job payload.
 */
@Component(service = JobConsumer.class,
        property = { JobConsumer.PROPERTY_TOPICS + "=com/aembootcamp/pagecreation" },
        immediate = true)
public class PageCreationJobConsumer implements JobConsumer {
    private final Logger logger = LoggerFactory.getLogger(PageCreationJobConsumer.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private void modifyComponentPokemonId(final Resource resource, final String path, final int pokemonId) {
        final Resource componentResource =
                resource.getChild(path);
        if (componentResource != null) {
            final ModifiableValueMap properties = componentResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                properties.put("pokemonId", pokemonId);
            }
        }
    }

    private void modifyTitle(final Resource resource, final String path, final String title) {
        final Resource componentResource =
                resource.getChild(path);
        if (componentResource != null) {
            final ModifiableValueMap properties = componentResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                properties.put("title", title);
            }
        }
    }

    /**
     * Processes a job to create a new page in Adobe Experience Manager (AEM) based on the properties
     * provided in the job. If the page creation is successful, the job's result is marked as successful.
     * If an error occurs during processing, the job result will indicate failure.
     *
     * @param job The job containing the properties required for page creation, such as pageTitle, parentPath,
     *            templatePath, and pageName.
     * @return JobResult.OK if the page creation is successful, otherwise JobResult.FAILED in case of an
     *         error during processing.
     */
    @Override
    public JobResult process(final Job job) {
        JobResult result = JobResult.FAILED;
        final String pageTitle = job.getProperty("pageTitle", String.class);
        final String parentPath = job.getProperty("parentPath", String.class);
        final String templatePath = job.getProperty("templatePath", String.class);
        final String pageName = job.getProperty("pageName", String.class);
        final int pokemonId = job.getProperty("pokemonId", Integer.class);

        try (ResourceResolver resolver = getServiceResolver()) {
            final PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                final Page page = pageManager.create(parentPath, pageName, templatePath, pageTitle);
                final Resource contentResource = page.getContentResource();
                modifyComponentPokemonId(contentResource, "root/container_982851773/dynamicpokemonstats", pokemonId);
                modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemondetail", pokemonId);
                modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemontype", pokemonId);
                modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemontype_956465200",
                        pokemonId);
                modifyTitle(contentResource, "root/container_1575998991/dynamicpokemontype", "Type");
                modifyTitle(contentResource, "root/container_1575998991/dynamicpokemontype_956465200", "Weakness");
                resolver.commit();
                if (logger.isInfoEnabled()) {
                    logger.info("Page created successfully at: {}", page.getPath());
                }
                result = JobResult.OK;
            }
        } catch (LoginException | WCMException | PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error creating page", e);
            }
        }
        return result;
    }


    private ResourceResolver getServiceResolver() throws LoginException {
        final Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "pageCreationService");
        return resolverFactory.getServiceResourceResolver(params);
    }

}
