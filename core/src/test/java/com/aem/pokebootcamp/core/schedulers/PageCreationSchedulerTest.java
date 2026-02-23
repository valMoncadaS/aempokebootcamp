package com.aem.pokebootcamp.core.schedulers;

import com.aem.pokebootcamp.core.config.PageCreationSchedulerConfig;
import junitx.util.PrivateAccessor;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PageCreationScheduler.
 */
@ExtendWith(MockitoExtension.class)
class PageCreationSchedulerTest {
    private static final int END_ID = 20;
    private static final int START_ID = 1;
    private static final int START_END_ID = 5;
    private static final String TARGET_PATH = "/content/aempokebootcamp/us/en/pokemons";
    private static final String SCHEDULER_EXPRESSION = "0 * * * * ?";
    private static final String PAGE_CREATION_PATH = "com/aembootcamp/pagecreation";


    @Mock
    private Scheduler scheduler;

    @Mock
    private JobManager jobManager;

    @Mock
    private PageCreationSchedulerConfig config;

    @Mock
    private ScheduleOptions scheduleOptions;

    @Mock
    private Logger logger;

    @InjectMocks
    private PageCreationScheduler schedulerUnderTest;


    @Test
    void activateScheduleDeactivateWithSameId() {
        when(config.startId()).thenReturn(START_ID);
        when(config.endId()).thenReturn(END_ID);
        when(config.targetPath()).thenReturn(TARGET_PATH);
        when(config.schedulerExpression()).thenReturn(SCHEDULER_EXPRESSION);
        when(scheduler.EXPR(SCHEDULER_EXPRESSION)).thenReturn(scheduleOptions);

        final ArgumentCaptor<String> unscheduleCaptor = ArgumentCaptor.forClass(String.class);

        schedulerUnderTest.activate(config);

        verify(scheduler, atLeastOnce()).unschedule(unscheduleCaptor.capture());
        verify(scheduler).schedule(eq(schedulerUnderTest), eq(scheduleOptions));

        final String capturedIdFromActivate = unscheduleCaptor.getValue();

        schedulerUnderTest.deactivate();

        verify(scheduler, atLeast(2)).unschedule(unscheduleCaptor.capture());
        final String capturedIdFromDeactivate = unscheduleCaptor.getAllValues()
                .get(unscheduleCaptor.getAllValues().size() - START_ID);
        assertEquals(capturedIdFromActivate, capturedIdFromDeactivate, "Deactivation should use same id as activation");
    }

    @Test
    void runEnqueuesJobsWithinTheRange() throws NoSuchFieldException {
        PrivateAccessor.setField(schedulerUnderTest, "logger", logger);
        when(logger.isInfoEnabled()).thenReturn(false);

        when(config.startId()).thenReturn(1);
        when(config.endId()).thenReturn(2);
        when(config.targetPath()).thenReturn(TARGET_PATH);
        when(config.schedulerExpression()).thenReturn("0 * * * * ?");
        when(scheduler.EXPR("0 * * * * ?")).thenReturn(scheduleOptions);

        when(jobManager.addJob(anyString(), any(Map.class))).thenReturn(null);

        schedulerUnderTest.activate(config);

        final ArgumentCaptor<Map<String, Object>> propsCaptor = ArgumentCaptor.forClass(Map.class);

        schedulerUnderTest.run();
        verify(jobManager, times(1)).addJob(eq(PAGE_CREATION_PATH), propsCaptor.capture());
        final Map<String, Object> firstProps = propsCaptor.getValue();

        assertEquals("Pokemon 1", firstProps.get("pageTitle"), "Page title should be Pokemon 1");
        assertEquals(TARGET_PATH, firstProps.get("parentPath"), "Parent path should be configured target path");
        assertEquals("/conf/aempokebootcamp/settings/wcm/templates/pokemon-detail-dynamic-page",
                firstProps.get("templatePath"), "Template path should be configured template path");
        assertEquals("pokemon-1", firstProps.get("pageName"), "Page name should be pokemon-1");
        assertEquals(1, firstProps.get("pokemonId"), "Pokemon id should be 1");

        schedulerUnderTest.run();
        verify(jobManager, times(2)).addJob(eq(PAGE_CREATION_PATH), propsCaptor.capture());
        final Map<String, Object> secondProps = propsCaptor.getValue();
        assertEquals("Pokemon 2", secondProps.get("pageTitle"), "Page title should be Pokemon 2");
        assertEquals("pokemon-2", secondProps.get("pageName"), "Page name should be pokemon-2");
        assertEquals(2, secondProps.get("pokemonId"), "Pokemon id should be 2");

        schedulerUnderTest.run();
        verify(jobManager, times(2)).addJob(eq(PAGE_CREATION_PATH), any(Map.class));
    }

    @Test
    void runInitializesWithDifferentExpression() {
        when(config.startId()).thenReturn(START_END_ID);
        when(config.endId()).thenReturn(START_END_ID);
        when(config.targetPath()).thenReturn(TARGET_PATH);
        when(config.schedulerExpression()).thenReturn("0 0 * * * ?");
        when(scheduler.EXPR("0 0 * * * ?")).thenReturn(scheduleOptions);

        when(jobManager.addJob(anyString(), any(Map.class))).thenReturn(null);

        schedulerUnderTest.activate(config);

        schedulerUnderTest.run();

        final ArgumentCaptor<Map<String, Object>> propsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(jobManager, times(1)).addJob(eq("com/aembootcamp/pagecreation"), propsCaptor.capture());
        final Map<String, Object> props = propsCaptor.getValue();
        assertEquals("Pokemon 5", props.get("pageTitle"), "Page title should be Pokemon 5");
        assertEquals("pokemon-5", props.get("pageName"), "Page name should be pokemon-5");
        assertEquals(START_END_ID, props.get("pokemonId"), "Pokemon id should be 5");

        schedulerUnderTest.run();
        verify(jobManager, times(1)).addJob(eq("com/aembootcamp/pagecreation"), any(Map.class));
    }
}
