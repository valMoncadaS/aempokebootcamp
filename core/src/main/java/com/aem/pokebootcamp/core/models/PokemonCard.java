package com.aem.pokebootcamp.core.models;

import java.util.List;

public interface PokemonCard {

  String getName();

  String getFileReference();

  List<String> getPokemonTypes();
}
