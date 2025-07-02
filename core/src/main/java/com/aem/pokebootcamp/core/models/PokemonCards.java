package com.aem.pokebootcamp.core.models;

import com.aem.pokebootcamp.core.models.impl.PokemonCardImpl;

import java.util.List;

public interface PokemonCards {
  List<PokemonCardImpl>  getPokemonCards();
}
