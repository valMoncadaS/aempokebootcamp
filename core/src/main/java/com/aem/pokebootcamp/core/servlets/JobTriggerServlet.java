package com.aem.pokebootcamp.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JobTriggerServlet is a POST-based Sling servlet that allows triggering
 * a range of jobs for page creation in Adobe Experience Manager (AEM).
 */
@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=aempokebootcamp/components/jobtrigger",
                "sling.servlet.selectors=runjob",
                "sling.servlet.extensions=json",
                "sling.servlet.methods=POST"
        }
)
public class JobTriggerServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(JobTriggerServlet.class);

    @Reference
    private JobManager jobManager;

    /**
     * Handles HTTP POST requests to trigger a range of jobs for creating pages.
     * The method retrieves parameters from the request to define the range of jobs
     * and their properties. It executes the job creation process based on the input
     * and provides corresponding JSON responses.
     *
     * @param request  the HTTP request containing parameters such as "basepath", "start", and "end"
     * @param response the HTTP response for writing the JSON output indicating success or failure
     * @throws IOException      if an I/O error occurs during processing
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {

        final Map<String, Object> jsonResponse = new HashMap<>();
        try {
            final String basePath = request.getParameter("basepath");
            final String rangeStart = request.getParameter("start");
            final String rangeEnd = request.getParameter("end");

            if (basePath == null || rangeStart == null || rangeEnd == null) {
                response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Missing required parameters: basepath, start, end");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            final int start = Integer.parseInt(rangeStart);
            final int end = Integer.parseInt(rangeEnd);

            for (int i = start; i <= end; i++) {
                final Map<String, Object> jobProps = new HashMap<>();
                jobProps.put("pageTitle", "Pokemon " + i);
                jobProps.put("parentPath", basePath);
                jobProps.put("templatePath",
                        "/conf/aempokebootcamp/settings/wcm/templates/pokemon-detail-dynamic-page");
                jobProps.put("pageName", "pokemon-" + i);
                jobProps.put("pokemonId", i);

                jobManager.addJob("com/aembootcamp/pagecreation", jobProps);
            }

            response.setStatus(SlingHttpServletResponse.SC_OK);
            jsonResponse.put("message", "Jobs triggered successfully for range " + start + " to " + end);
        } catch (IOException | NumberFormatException e) {
            logger.error("Error triggering job", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", e.getMessage());
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}
