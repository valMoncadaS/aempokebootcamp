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
        final Resource componentResource = resource.getChild(path);
        if (componentResource != null) {
            final ModifiableValueMap properties = componentResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                properties.put("pokemonId", pokemonId);
            }
        }
    }

    private void modifyStringProperty(final Resource resource, final String path,
                                      final String title, final String property) {
        final Resource componentResource =
                resource.getChild(path);
        if (componentResource != null) {
            final ModifiableValueMap properties = componentResource.adaptTo(ModifiableValueMap.class);
            if (properties != null) {
                properties.put(property, title);
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
        final String parentPath = job.getProperty("parentPath", String.class);
        final String pageName = job.getProperty("pageName", String.class);

        try (ResourceResolver resolver = getServiceResolver()) {
            final PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                final Resource page = resolver.getResource(parentPath + "/" + pageName);
                if (page != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Page already exists at: {}", page.getPath());
                    }
                } else {
                    createPage(pageManager, job, resolver);
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

    private void createPage(final PageManager pageManager, final Job job, final ResourceResolver resolver)
            throws WCMException, PersistenceException {
        final String parentPath = job.getProperty("parentPath", String.class);
        final String pageName = job.getProperty("pageName", String.class);
        final String pageTitle = job.getProperty("pageTitle", String.class);
        final String templatePath = job.getProperty("templatePath", String.class);
        final int pokemonId = job.getProperty("pokemonId", Integer.class);

        final Page page = pageManager.create(parentPath, pageName, templatePath, pageTitle);
        final Resource contentResource = page.getContentResource();
        final String imageSrc = "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/full/"
                                + String.format("%03d", pokemonId) + ".png";
        modifyComponentPokemonId(contentResource, "root/container_982851773/dynamicpokemonstats", pokemonId);
        modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemondetail", pokemonId);
        modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemontype", pokemonId);
        modifyComponentPokemonId(contentResource, "root/container_1575998991/dynamicpokemontype_956465200",
                pokemonId);
        modifyStringProperty(contentResource, "root/container_1575998991/dynamicpokemontype", "Type",
                "title");
        modifyStringProperty(contentResource, "root/container_1575998991/dynamicpokemontype_956465200",
                "Weakness", "title");
        modifyStringProperty(contentResource, "root/container_982851773/image", imageSrc, "imageUrl");
        resolver.commit();
        if (logger.isInfoEnabled()) {
            logger.info("Page created successfully at: {}", page.getPath());
        }
    }


    private ResourceResolver getServiceResolver() throws LoginException {
        final Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "pageCreationService");
        return resolverFactory.getServiceResourceResolver(params);
    }

}
