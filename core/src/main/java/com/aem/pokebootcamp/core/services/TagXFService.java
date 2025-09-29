package com.aem.pokebootcamp.core.services;

import java.util.List;

/**
 * Represents a service interface for retrieving Experience Fragments (XFs)
 * based on specified tag IDs.
 *
 * This service provides a method to fetch a list of Experience Fragment
 * objects that are associated with the given list of tag IDs. Each tag ID
 * corresponds to a specific tag within the system. The service performs
 * the necessary operations to resolve these tags and find Experience
 * Fragments that are tagged accordingly.
 *
 * Methods:
 * - `getXFsByTags`: Accepts a list of tag IDs and returns a list of
 *   Experience Fragment objects that match the specified tags.
 *
 * Usage of this interface is client-specific and relies on its implementations
 * to handle the underlying resource resolution and tag matching functionalities.
 */
public interface TagXFService {
    /**
     * Retrieves a list of Experience Fragments that are associated with the specified tags.
     *
     * This method accepts a list of tag IDs and returns a collection of ExperienceFragment
     * objects that are linked to those tags. The relationship between tags and Experience
     * Fragments is resolved by the service implementation. An empty list will be returned
     * if no Experience Fragment matches the provided tag IDs.
     *
     * @param tagIds a list of tag IDs used to filter and retrieve associated Experience Fragments
     * @return a list of ExperienceFragment objects that match the specified tag IDs
     */
    List<String> getXFsByTags(List<String> tagIds);
}
