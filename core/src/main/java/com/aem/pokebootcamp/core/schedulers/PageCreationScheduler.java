
package com.aem.pokebootcamp.core.schedulers;

import com.aem.pokebootcamp.core.config.PageCreationSchedulerConfig;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * The PageCreationScheduler class is responsible for scheduling and managing
 * the creation of pages dynamically in Adobe Experience Manager (AEM).
 * The configuration for this scheduler is provided through the
 * {@link PageCreationSchedulerConfig} interface, which allows defining parameters
 * such as start and end IDs, the parent path for page creation, and the CRON expression.
 */
@Component(
        service = Runnable.class,
        immediate = true,
        configurationPolicy = ConfigurationPolicy.REQUIRE
)
@Designate(ocd = PageCreationSchedulerConfig.class)
public class PageCreationScheduler implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(PageCreationScheduler.class);
    private static final String JOB_TOPIC = "com/aembootcamp/pagecreation";

    @Reference
    private Scheduler scheduler;

    @Reference
    private JobManager jobManager;

    private int startId;
    private int endId;
    private String targetPath;
    private int schedulerId;
    private int positionId;

    /**
     * Activates and configures the Page Creation Scheduler based on the provided configuration.
     * This method is triggered during the activation and modification of the OSGi component. It initializes
     * the scheduler with the specified parameters, unschedules any existing tasks for the current
     * scheduler instance, and schedules the task using the CRON expression from the configuration.
     *
     * @param config the configuration object that provides the scheduling parameters, including:
     *               - {@code startId}: the starting ID for the range of page creation.
     *               - {@code endId}: the ending ID for the range of page creation.
     *               - {@code targetPath}: the parent path where pages will be created.
     *               - {@code schedulerExpression}: the CRON expression defining the task execution frequency.
     */
    @Activate
    @Modified
    protected void activate(final PageCreationSchedulerConfig config) {
        this.startId = config.startId();
        this.endId = config.endId();
        this.targetPath = config.targetPath();
        final String cronExpression = config.schedulerExpression();

        schedulerId = hashCode();

        scheduler.unschedule(String.valueOf(schedulerId));

        scheduler.schedule(
                this,
                scheduler.EXPR(cronExpression)
        );

        logger.info("PageCreationScheduler activated with CRON [{}]", cronExpression);
    }

    /**
     * Deactivates the scheduler by unscheduling the scheduled task identified by the {@code schedulerId}.
     * This method is invoked when the OSGi component is deactivated. It ensures that any scheduled
     * task associated with the current instance of the scheduler is properly cleaned up, preventing
     * potential resource leaks or unintended execution after the component is removed.
     */
    @Deactivate
    protected void deactivate() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    /**
     * Executes the scheduled task that dynamically initiates page creation jobs.
     * This method is triggered based on the CRON expression configured for the
     * scheduler. It uses the JobManager to enqueue jobs for creating pages with
     * properties specified dynamically for each ID within the configured range.
     */
    @Override
    public void run() {
        if (logger.isInfoEnabled()) {
            logger.info("PageCreationScheduler executed at {}", Instant.now());
        }

        if (positionId < startId) {
            positionId = startId;
        }
        if (positionId <= endId) {
            final Map<String, Object> jobProps = new HashMap<>();
            jobProps.put("pageTitle", "Pokemon " + positionId);
            jobProps.put("parentPath", targetPath);
            jobProps.put("templatePath",
                    "/conf/aempokebootcamp/settings/wcm/templates/pokemon-detail-dynamic-page");
            jobProps.put("pageName", "pokemon-" + positionId);
            jobProps.put("pokemonId", positionId);
            positionId++;
            jobManager.addJob(JOB_TOPIC, jobProps);
        }
    }
}
