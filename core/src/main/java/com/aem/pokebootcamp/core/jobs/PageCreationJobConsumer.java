package com.aem.pokebootcamp.core.jobs;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
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

        try (ResourceResolver resolver = getServiceResolver()) {
            final PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                final Page page = pageManager.create(parentPath, pageName, templatePath, pageTitle);
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
