package com.pokemonreview.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokemonreview.api.models.Pokemon;

public interface TestRepoPokemon extends JpaRepository<Pokemon , Integer>{
    
}
