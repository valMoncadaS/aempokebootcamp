package com.aem.pokebootcamp.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Represents a Data Transfer Object (DTO) for a Pokemon.
 * Provides encapsulation of various Pokemon attributes that include general characteristics,
 */
@Builder
public class PokemonDTO {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<String> types;

    @Getter
    @Setter
    private List<String> weakness;

    @Getter
    @Setter
    private String height;

    @Getter
    @Setter
    private String weight;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private List<String> abilities;

    @Getter
    @Setter
    private List<String> gender;

    @Getter
    @Setter
    private Map<String, Integer> stats;

    @Getter
    @Setter
    private String pokemonImage;
}
