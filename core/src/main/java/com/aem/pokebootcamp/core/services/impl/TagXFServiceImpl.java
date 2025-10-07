package com.aem.pokebootcamp.core.services.impl;

import com.aem.pokebootcamp.core.services.TagXFService;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.constants.NameConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;


/**
 * Implementation of the {@link TagXFService} interface that provides functionality for retrieving
 * Experience Fragment (XF) paths based on associated tags. This class interacts with the repository
 * to fetch XF resources and processes them to return a list of unique XF paths that match with the provided tagId
 */
@Component(service = TagXFService.class)
public class TagXFServiceImpl implements TagXFService {
    private final Logger log = LoggerFactory.getLogger(TagXFServiceImpl.class);
    private static final String XF_SEARCH_ROOT = "/content/experience-fragments/aempokebootcamp/us/en/site/type";
    private static final String TAG_PREFIX = "pokemon-types:";
    private static final String XF_RESOURCE_TYPE = "aempokebootcamp/components/xfpage";

    @Reference
    private ResourceResolverFactory resolverFactory;


    /**
     * Retrieves a list of Experience Fragment (XF) paths associated with the specified tag IDs.
     * This method fetches Experience Fragments that are tagged with the provided tag IDs by
     * resolving resources and searching within the repository. A resource resolver is used
     * for the operation, and the matching XF paths are returned as a list. If an error occurs
     * during the resource resolution or repository access, it is logged, and an empty list
     * is returned.
     *
     * @param tagIds a list of tag IDs used to filter and retrieve associated Experience Fragment paths
     * @return a list of Experience Fragment paths matching the specified tags, or an empty list if no matches are found
     */
    @Override
    public List<String> getXFsByTags(final List<String> tagIds) {
        final Map<String, Object> authInfo = new HashMap<>();
        authInfo.put(ResourceResolverFactory.SUBSERVICE, "tagxf-service-user");
        List<String> xfs = new ArrayList<>();

        try (ResourceResolver resolver = resolverFactory.getResourceResolver(authInfo)) {
            xfs = findXfPathsForTags(resolver, tagIds);
        } catch (LoginException | RepositoryException e) {
            log.error("Error obtaining resource resolver", e);
        }

        return xfs;
    }

    /**
     * Retrieves a list of Experience Fragment (XF) paths based on the provided tag IDs.
     * This method searches the repository for Experience Fragments associated with the given tags.
     * It uses a QueryBuilder to construct and execute queries for retrieving relevant resources
     * and processes the results to extract unique XF paths. If the QueryBuilder or Session
     * cannot be obtained, an empty list is returned. Any errors encountered during repository
     * access are logged.
     *
     * @param resourceResolver the ResourceResolver used to resolve resources within the repository
     * @param tagIds           a list of tag IDs used to search for associated XF paths
     * @return a list of unique Experience Fragment paths that match the specified tags,
     *         or an empty list if no matches are found
     * @throws RepositoryException if an error occurs while accessing the repository
     */
    private List<String> findXfPathsForTags(final ResourceResolver resourceResolver, final List<String> tagIds)
            throws RepositoryException {
        final Set<String> uniqueXfPaths = new HashSet<>();
        final QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        final Session session = resourceResolver.adaptTo(Session.class);

        if (queryBuilder != null && session != null) {
            for (final String tag : tagIds) {
                final String fullTagId = tag.startsWith(TAG_PREFIX) ? tag : TAG_PREFIX + tag;
                final SearchResult result = getXFsResources(fullTagId, queryBuilder, session);

                validateXFs(result, uniqueXfPaths, resourceResolver, fullTagId);
            }
        } else {
            log.warn("Unable to get QueryBuilder or Session, returning empty list");
        }

        final List<String> result = new ArrayList<>(uniqueXfPaths);
        if (log.isInfoEnabled()) {
            log.info("Found {} unique XF paths for tags: {}", result.size(), tagIds);
        }
        return result;
    }

    /**
     * Executes a search query to retrieve resources associated with a specified tag ID.
     * This method leverages a QueryBuilder to construct and execute a query
     * for resources in the repository that match the provided tag ID. The query
     * is configured with specific predicates such as resource path, type, and property
     * value constraints. The result of the query is returned as a SearchResult object.
     *
     * @param fullTagId    the fully qualified tag ID used as a filter criteria
     * @param queryBuilder the QueryBuilder instance used to construct and execute the query
     * @param session      the current Session instance for accessing the repository
     * @return a SearchResult object containing the resources matching the specified tag ID
     */
    private SearchResult getXFsResources(final String fullTagId,
                                         final QueryBuilder queryBuilder,
                                         final Session session) {

        log.debug("Searching for XFs with weakness tag: {}", fullTagId);
        final Map<String, String> predicates = new HashMap<>();
        predicates.put("path", XF_SEARCH_ROOT);
        predicates.put("type", JcrConstants.NT_UNSTRUCTURED);
        predicates.put("property", NameConstants.PN_TAGS);
        predicates.put("property.value", fullTagId);
        predicates.put("p.limit", "-1");
        predicates.put("p.nodedepth", "10");

        final Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        return query.getResult();
    }

    /**
     * Validates and processes the search results to identify Experience Fragment (XF) variations
     * and adds their paths to a set of unique paths.
     * This method iterates through the hits in the provided search result, retrieves the XF
     * variation for each hit, and adds its path to the set of unique XF paths. If an exception
     * is encountered while processing a hit, it is logged without interrupting the iteration.
     *
     * @param result           the SearchResult object containing the search hits to be processed
     * @param uniqueXfPaths    a set to store the unique paths of identified XF variations
     * @param resourceResolver the ResourceResolver instance used to retrieve resources
     * @param fullTagId        the fully qualified tag ID associated with the search
     * @throws RepositoryException if an error occurs while accessing the repository
     */
    private void validateXFs(final SearchResult result,
                             final Set<String> uniqueXfPaths,
                             final ResourceResolver resourceResolver,
                             final String fullTagId) throws RepositoryException {
        for (final Hit hit : result.getHits()) {
            final Resource hitResource = resourceResolver.getResource(hit.getPath());
            final Resource xfVariation = findXfVariation(hitResource);

            if (xfVariation != null) {
                uniqueXfPaths.add(xfVariation.getPath());
                if (log.isDebugEnabled()) {
                    log.debug("Found XF variation: {} for tag: {}", xfVariation.getPath(), fullTagId);
                }
            }
        }
    }


    /**
     * Finds the Experience Fragment (XF) variation for a given resource by traversing
     * up the resource hierarchy until an XF variation is identified or the root resource is reached.
     * The method checks if the resource or any of its parent resources is of type "cq:Page"
     * or has a super type of "cq:Page". If such a resource contains a "jcr:content" child
     * with a "sling:resourceType" property matching the predefined XF_RESOURCE_TYPE,
     * it is considered an XF variation.
     *
     * @param resource the starting resource from which to locate the XF variation
     * @return the resource representing the XF variation, or null if no XF variation is found
     */
    private Resource findXfVariation(final Resource resource) {
        Resource current = resource;

        while (current != null) {
            final String resourceType = current.getResourceType();
            final String resourceSuperType = current.getResourceSuperType();

            if (NameConstants.NT_PAGE.equals(resourceType) || NameConstants.NT_PAGE.equals(resourceSuperType)) {
                final Resource jcrContent = current.getChild(JcrConstants.JCR_CONTENT);
                if (jcrContent != null) {
                    final ValueMap properties = jcrContent.getValueMap();
                    final String slingResourceType = properties.get("sling:resourceType", "");

                    if (XF_RESOURCE_TYPE.equals(slingResourceType)) {
                        break;
                    }
                }
            }

            current = current.getParent();
        }

        return current;
    }
}
