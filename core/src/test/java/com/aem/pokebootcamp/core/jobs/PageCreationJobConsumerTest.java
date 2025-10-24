package com.aem.pokebootcamp.core.jobs;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * Unit test class for {@link PageCreationJobConsumer}.
 * This class validates the functionality of the PageCreationJobConsumer by using mock dependencies
 * and ensures that it processes job payloads correctly to create pages in Adobe Experience Manager (AEM).
 */
@ExtendWith(MockitoExtension.class)
class PageCreationJobConsumerTest {
    private static final String PAGE_TITLE = "Test Page";
    private static final String PARENT_PATH = "/content/aempokebootcamp";
    private static final String TEMPLATE_PATH = "/conf/aempokebootcamp/settings/wcm/templates/page-template";
    private static final String PAGE_NAME = "test-page";
    private static final String PAGE_TITLE_ATTRIBUTE = "pageTitle";
    private static final String PARENT_PATH_ATTRIBUTE = "parentPath";
    private static final String TEMPLATE_PATH_ATTRIBUTE = "templatePath";
    private static final String PAGE_NAME_ATTRIBUTE = "pageName";

    @InjectMocks
    private PageCreationJobConsumer jobConsumer;

    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private PageManager pageManager;

    @Mock
    private Page page;

    @Mock
    private Job job;

    @Mock
    private Logger logger;


    @BeforeEach
    void setUp() throws LoginException {
        Mockito.when(resolverFactory.getServiceResourceResolver(any())).thenReturn(resourceResolver);
        Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
    }

    @Test
    void processJobSuccessfully() throws PersistenceException, WCMException {
        Mockito.when(pageManager.create(PARENT_PATH,
                PAGE_NAME,
                TEMPLATE_PATH,
                PAGE_TITLE))
                .thenReturn(page);

        Mockito.when(job.getProperty(PAGE_TITLE_ATTRIBUTE, String.class)).thenReturn(PAGE_TITLE);
        Mockito.when(job.getProperty(PARENT_PATH_ATTRIBUTE, String.class)).thenReturn(PARENT_PATH);
        Mockito.when(job.getProperty(TEMPLATE_PATH_ATTRIBUTE, String.class)).thenReturn(TEMPLATE_PATH);
        Mockito.when(job.getProperty(PAGE_NAME_ATTRIBUTE, String.class)).thenReturn(PAGE_NAME);

        Mockito.doNothing().when(resourceResolver).commit();

        final JobConsumer.JobResult result = jobConsumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result, "Expected JobResult.OK when the page creation is successful");
        Mockito.verify(pageManager, times(1)).create(any(), any(), any(), any());
        Mockito.verify(resourceResolver, times(1)).commit();
    }

    @Test
    void testProcessJobFailureWhenPageManagerIsNull() {
        Mockito.when(resourceResolver.adaptTo(PageManager.class)).thenReturn(null);

        final JobConsumer.JobResult result = jobConsumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result, "Expected JobResult.FAILED when the page manager is null");
    }

    @Test
    void processJobWithIsInfoEnabledFalse() throws WCMException, PersistenceException, NoSuchFieldException {
        PrivateAccessor.setField(jobConsumer, "logger", logger);
        Mockito.when(logger.isInfoEnabled()).thenReturn(false);

        Mockito.when(pageManager.create(PARENT_PATH,
                        PAGE_NAME,
                        TEMPLATE_PATH,
                        PAGE_TITLE))
                .thenReturn(page);

        Mockito.when(job.getProperty(PAGE_TITLE_ATTRIBUTE, String.class)).thenReturn(PAGE_TITLE);
        Mockito.when(job.getProperty(PARENT_PATH_ATTRIBUTE, String.class)).thenReturn(PARENT_PATH);
        Mockito.when(job.getProperty(TEMPLATE_PATH_ATTRIBUTE, String.class))
                .thenReturn(TEMPLATE_PATH);
        Mockito.when(job.getProperty(PAGE_NAME_ATTRIBUTE, String.class)).thenReturn(PAGE_NAME);

        Mockito.doNothing().when(resourceResolver).commit();

        final JobConsumer.JobResult result = jobConsumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result, "Expected JobResult.OK when the page creation is successful");
    }

    @Test
    void processJobWithIsErrorEnabledFalse() throws WCMException, NoSuchFieldException {
        PrivateAccessor.setField(jobConsumer, "logger", logger);
        Mockito.when(logger.isErrorEnabled()).thenReturn(false);

        Mockito.when(pageManager.create(PARENT_PATH,
                        PAGE_NAME,
                        TEMPLATE_PATH,
                        PAGE_TITLE))
                .thenThrow(new WCMException("Test Exception"));

        Mockito.when(job.getProperty(PAGE_TITLE_ATTRIBUTE, String.class)).thenReturn(PAGE_TITLE);
        Mockito.when(job.getProperty(PARENT_PATH_ATTRIBUTE, String.class)).thenReturn(PARENT_PATH);
        Mockito.when(job.getProperty(TEMPLATE_PATH_ATTRIBUTE, String.class))
                .thenReturn(TEMPLATE_PATH);
        Mockito.when(job.getProperty(PAGE_NAME_ATTRIBUTE, String.class)).thenReturn(PAGE_NAME);

        final JobConsumer.JobResult result = jobConsumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result,
                "Expected JobResult.FAILED when there is an exception while creating the page");
    }

    @Test
    void processJobThrowingError() throws WCMException, NoSuchFieldException {
        PrivateAccessor.setField(jobConsumer, "logger", logger);
        Mockito.when(logger.isErrorEnabled()).thenReturn(true);

        Mockito.when(pageManager.create(PARENT_PATH,
                        PAGE_NAME,
                        TEMPLATE_PATH,
                        PAGE_TITLE))
                .thenThrow(new WCMException("Test Exception"));

        Mockito.when(job.getProperty(PAGE_TITLE_ATTRIBUTE, String.class)).thenReturn(PAGE_TITLE);
        Mockito.when(job.getProperty(PARENT_PATH_ATTRIBUTE, String.class)).thenReturn(PARENT_PATH);
        Mockito.when(job.getProperty(TEMPLATE_PATH_ATTRIBUTE, String.class))
                .thenReturn(TEMPLATE_PATH);
        Mockito.when(job.getProperty(PAGE_NAME_ATTRIBUTE, String.class)).thenReturn(PAGE_NAME);

        final JobConsumer.JobResult result = jobConsumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result,
                "Expected JobResult.FAILED when there is an exception while creating the page");
    }
}
