package com.aem.pokebootcamp.core.servlets;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link JobTriggerServlet} class.
 * This test class verifies the behavior of the servlet, including handling of valid and invalid inputs,
 * response status codes, and invocation of the {@link JobManager} for job triggering.
 */
@ExtendWith(MockitoExtension.class)
class JobTriggerServletTest {
    private static final int INVOCATION_NUMBER = 3;
    private static final String MISSING_REQUIRED_PARAMETERS = "Missing required parameters";
    private static final String EXPECTED_ERROR = "Expected error message in response";
    private static final String BASE_PATH = "basepath";
    private static final String RANGE_START = "start";
    private static final String RANGE_END = "end";

    @InjectMocks
    private JobTriggerServlet servlet;

    @Mock
    private JobManager jobManager;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();

        final PrintWriter writer = new PrintWriter(responseWriter);

        when(response.getWriter()).thenReturn(writer);
    }


    @Test
    void missingParametersReturns400() throws IOException {

        servlet.doPost(request, response);

        verify(response).setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains(MISSING_REQUIRED_PARAMETERS),
                EXPECTED_ERROR);
    }

    @Test
    void missingRangeEndReturns400() throws IOException {
        when(request.getParameter(BASE_PATH)).thenReturn("/content/test");
        when(request.getParameter(RANGE_START)).thenReturn("1");

        servlet.doPost(request, response);

        verify(response).setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains(MISSING_REQUIRED_PARAMETERS),
                EXPECTED_ERROR);
    }

    @Test
    void missingRangeStartReturns400() throws IOException {
        when(request.getParameter(BASE_PATH)).thenReturn("/content/test");
        when(request.getParameter(RANGE_START)).thenReturn(null);
        when(request.getParameter(RANGE_END)).thenReturn("3");

        servlet.doPost(request, response);

        verify(response).setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains(MISSING_REQUIRED_PARAMETERS),
                EXPECTED_ERROR);
    }

    @Test
    void validRequestTriggersJobsAndReturns200() throws IOException {
        when(request.getParameter(BASE_PATH)).thenReturn("/content/test");
        when(request.getParameter(RANGE_START)).thenReturn("1");
        when(request.getParameter(RANGE_END)).thenReturn("3");

        servlet.doPost(request, response);

        verify(response).setStatus(SlingHttpServletResponse.SC_OK);
        assertTrue(responseWriter.toString().contains("Jobs triggered successfully"),
                "Expected success message in response");

        verify(jobManager, times(INVOCATION_NUMBER)).addJob(eq("com/aembootcamp/pagecreation"), anyMap());
    }

    @Test
    void exceptionReturns500() throws IOException {
        when(request.getParameter(BASE_PATH)).thenReturn("test");
        when(request.getParameter(RANGE_START)).thenReturn("a"); // Invalid number
        when(request.getParameter(RANGE_END)).thenReturn("3");
        servlet.doPost(request, response);

        verify(response).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertTrue(responseWriter.toString().contains("error"), EXPECTED_ERROR);
    }
}
