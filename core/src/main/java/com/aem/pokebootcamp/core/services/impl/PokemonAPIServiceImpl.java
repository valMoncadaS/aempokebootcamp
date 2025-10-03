package com.aem.pokebootcamp.core.services.impl;

import com.aem.pokebootcamp.core.dto.PokemonDTO;
import com.aem.pokebootcamp.core.services.PokemonAPIService;
import com.aem.pokebootcamp.core.services.PokemonConfigMethods;
import lombok.extern.slf4j.Slf4j;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;
import me.sargunvohra.lib.pokekotlin.model.PokemonAbility;
import me.sargunvohra.lib.pokekotlin.model.PokemonStat;
import me.sargunvohra.lib.pokekotlin.model.Genus;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PokemonAPIService} interface.
 * Provides functionality to retrieve and process Pokemon data using a client for an external API.
 */
@Slf4j
@Component(service = PokemonAPIService.class)
public class PokemonAPIServiceImpl implements PokemonAPIService {
    private static final int MAX_GENRE_NUMBER = 8;

    @Reference
    private PokemonConfigMethods pokemonConfigMethods;

    /**
     * Retrieves the base URL used by the Pokémon API service.
     *
     * @return a string representing the Pokémon API base URL.
     */
    @Override
    public String getExternalLink() {
        return pokemonConfigMethods.getUrl();
    }

    /**
     * Retrieves detailed information about a Pokémon based on its unique identifier.
     *
     * @param idPokemon the unique identifier of the Pokémon to be retrieved
     * @return an instance of {@link PokemonDTO} containing detailed information
     *         about the Pokémon, including its name, types, abilities, height,
     *         weight, stats, gender, weaknesses, and category
     */
    @Override
    public PokemonDTO getPokemon(final int idPokemon) {
        final PokeApiClient client = new PokeApiClient();
        final Pokemon pokemon = client.getPokemon(idPokemon);
        final PokemonSpecies species = client.getPokemonSpecies(idPokemon);


        final List<String> types = pokemon.getTypes().stream()
                .map(PokemonType::getType)
                .map(NamedApiResource::getName)
                .collect(Collectors.toList());

        final List<String> weaknesses = pokemon.getTypes().stream()
                .map(PokemonType::getType)
                .map(type -> client.getType(type.getId()).getDamageRelations().getNoDamageTo())
                .map(noDamage ->
                        noDamage.stream().map(NamedApiResource::getName).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        final List<String> abilities = pokemon.getAbilities().stream()
                .map(PokemonAbility::getAbility)
                .map(NamedApiResource::getName)
                .collect(Collectors.toList());

        final Map<String, Integer> stats = pokemon.getStats().stream()
                .collect(Collectors.toMap(
                        stat -> stat.getStat().getName(),
                        PokemonStat::getBaseStat
                ));

        final List<String> gender = resolveGender(species.getGenderRate());

        final String category = species.getGenera().stream()
                .filter(genus -> "en".equals(genus.getLanguage().getName()))
                .map(Genus::getGenus)
                .findFirst()
                .orElse("Unknown").replace("Pokémon", "");


        return PokemonDTO.builder()
                .name(pokemon.getName())
                .types(types)
                .abilities(abilities)
                .height(String.valueOf(pokemon.getHeight()))
                .weight(String.valueOf(pokemon.getWeight()))
                .stats(stats)
                .gender(gender)
                .weakness(weaknesses)
                .category(category)
                .build();
    }

    /**
     * Resolves the possible genders of a Pokémon based on its gender rate.
     *
     * @param genderRate an integer representing the Pokémon's gender rate. A value of -1
     *                   indicates the Pokémon is genderless. Values between 0 and 8
     *                   define the ratio of male to female, where 0 is fully male and 8 is fully female.
     * @return a list of strings representing the possible genders of the Pokémon.
     *         It will contain "Genderless" if the gender rate is -1, or a combination
     *         of "Male" and/or "Female" based on the gender rate value.
     */
    private List<String> resolveGender(final int genderRate) {
        final List<String> genders = new ArrayList<>();
        if (genderRate == -1) {
            genders.add("Genderless");
        }

        if (genderRate > 0) {
            genders.add("Female");
        }
        if (genderRate < MAX_GENRE_NUMBER) {
            genders.add("Male");
        }

        return genders;
    }

}
