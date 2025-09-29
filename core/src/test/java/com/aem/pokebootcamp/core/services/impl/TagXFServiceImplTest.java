package com.aem.pokebootcamp.core.services.impl;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit test class for the TagXFServiceImpl class.
 * This test class validates the behavior of the TagXFServiceImpl service
 * by executing various test cases that simulate different conditions
 * and permutations of inputs to ensure robust handling of tag-based
 * experience fragment retrieval functionality.
 * It utilizes the AEM Context, JUnit 5 testing framework, and Mockito for mocking
 * external dependencies.
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class TagXFServiceImplTest {

    private final AemContext context = new AemContext();
    private static final String EXPECTED_NOT_NULL = "Expected non-null result from getXFsByTags";
    private static final String FIRE = "fire";
    private static final String MESSAGE_EMPTY = "Expected empty result from getXFsByTags";
    private static final String POKEMON_TYPES_FIRE = "pokemon-types:fire";
    private static final String FIRE_XF_PATH = "/content/experience-fragments/aempokebootcamp/us/en/site/type/fire";
    private static final String EXPECTED_ONE_RESULT = "Expected 1 result from getXFsByTags";
    private static final String EXPECTED_FIRE_PATH = "Expected result to be the path of the test fire XF";

    @InjectMocks
    private TagXFServiceImpl tagXFService;

    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private QueryBuilder queryBuilder;

    @Mock
    private Session session;

    @Mock
    private Query query;

    @Mock
    private SearchResult searchResult;

    @Mock
    private Hit hit;

    @Mock
    private Logger logger;


    @BeforeEach
    void setup() {
        // Load any common test resources if needed
        context.load().json("/com/aem/pokebootcamp/core/services/TagXFServiceImplTest.json",
                "/content/experience-fragments/aempokebootcamp/us/en/site");
    }

    @Test
    void testGetXFsByTags() throws RepositoryException, LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));

        when(hit.getPath()).thenReturn(FIRE_XF_PATH);
        when(resourceResolver.getResource(FIRE_XF_PATH))
                .thenReturn(context.resourceResolver()
                        .getResource(FIRE_XF_PATH));

        final List<String> result = tagXFService.getXFsByTags(List.of(POKEMON_TYPES_FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertEquals(1, result.size(), EXPECTED_ONE_RESULT);
        assertEquals(FIRE_XF_PATH, result.get(0), EXPECTED_FIRE_PATH);
    }

    @Test
    void testGetXFsByTagsInfoEnabled() throws RepositoryException, LoginException, NoSuchFieldException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));
        when(hit.getPath()).thenReturn(FIRE_XF_PATH);
        when(resourceResolver.getResource(FIRE_XF_PATH))
                .thenReturn(context.resourceResolver()
                        .getResource(FIRE_XF_PATH));

        PrivateAccessor.setField(tagXFService, "log", logger);
        when(logger.isInfoEnabled()).thenReturn(false);

        final List<String> result = tagXFService.getXFsByTags(List.of(POKEMON_TYPES_FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertEquals(1, result.size(), EXPECTED_ONE_RESULT);
        assertEquals(FIRE_XF_PATH, result.get(0), EXPECTED_FIRE_PATH);
    }


    @Test
    void testGetXFsByTagsWithChildNode() throws RepositoryException, LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));

        final String testJcrContent = "/content/experience-fragments/aempokebootcamp/us/en/site/type/fire/jcr:content";
        when(hit.getPath()).thenReturn(testJcrContent);
        when(resourceResolver.getResource(testJcrContent))
                .thenReturn(context.resourceResolver()
                        .getResource(testJcrContent));

        final List<String> result = tagXFService.getXFsByTags(List.of(POKEMON_TYPES_FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertEquals(1, result.size(), EXPECTED_ONE_RESULT);
        assertEquals(FIRE_XF_PATH, result.get(0), EXPECTED_FIRE_PATH);
    }

    @Test
    void testGetXFsByTagsQueryBuilderNull() throws LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(null);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);


        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertTrue(result.isEmpty(), MESSAGE_EMPTY);
    }

    @Test
    void testGetXFsByTagsSessionNull() throws LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(null);


        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertTrue(result.isEmpty(), MESSAGE_EMPTY);
    }

    @Test
    void testGetXFsByTagsLoginException() throws LoginException {
        when(resolverFactory.getResourceResolver(any())).thenThrow(new LoginException("Login failed"));

        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertTrue(result.isEmpty(), MESSAGE_EMPTY);
    }

    @Test
    void testGetXFsByTagsWithSimpleTagId() throws RepositoryException, LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));

        when(hit.getPath()).thenReturn(FIRE_XF_PATH);
        when(resourceResolver.getResource(FIRE_XF_PATH))
                .thenReturn(context.resourceResolver()
                        .getResource(FIRE_XF_PATH));

        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertEquals(1, result.size(), EXPECTED_ONE_RESULT);
        assertEquals(FIRE_XF_PATH, result.get(0), EXPECTED_FIRE_PATH);
    }

    @Test
    void testGetXFsByTagsWithXfVariationNull() throws RepositoryException, LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));

        when(hit.getPath()).thenReturn(FIRE_XF_PATH);
        when(resourceResolver.getResource(FIRE_XF_PATH))
                .thenReturn(null);

        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertTrue(result.isEmpty(), MESSAGE_EMPTY);
    }

    @Test
    void testGetXFsByTagsWithInvalidPath() throws RepositoryException, LoginException {
        when(resolverFactory.getResourceResolver(any())).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(), eq(session))).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(List.of(hit));
        when(hit.getPath()).thenReturn(FIRE_XF_PATH);
        when(resourceResolver.getResource(FIRE_XF_PATH))
                .thenReturn(context.resourceResolver()
                        .getResource(FIRE_XF_PATH + "NotCqPage"));

        final List<String> result = tagXFService.getXFsByTags(List.of(FIRE));

        assertNotNull(result, EXPECTED_NOT_NULL);
        assertTrue(result.isEmpty(), MESSAGE_EMPTY);
    }

}
