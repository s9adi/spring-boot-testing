package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();
        PokemonDto pokemonDto = PokemonDto.builder().name("pickachu").type("electric").build();

        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnsResponseDto() {
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse savePokemon = pokemonService.getAllPokemon(1, 10);

        Assertions.assertThat(savePokemon).isNotNull();
    }

    @Test
    public void PokemonService_FindById_ReturnPokemonDto() {
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder().id(1).name("pikachu").type("electric").type("this is a type").build();
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));

        PokemonDto pokemonReturn = pokemonService.getPokemonById(pokemonId);

        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void PokemonService_UpdatePokemon_ReturnPokemonDto() {
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder().id(1).name("pikachu").type("electric").type("this is a type").build();
        PokemonDto pokemonDto = PokemonDto.builder().id(1).name("pikachu").type("electric").type("this is a type")
                .build();

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));
        when(pokemonRepository.save(pokemon)).thenReturn(pokemon);

        PokemonDto updateReturn = pokemonService.updatePokemon(pokemonDto, pokemonId);

        Assertions.assertThat(updateReturn).isNotNull();
    }

    @Test
    public void PokemonService_DeletePokemonById_ReturnVoid() {
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder().id(1).name("pikachu").type("electric").type("this is a type").build();

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));
        doNothing().when(pokemonRepository).delete(pokemon);

        assertAll(() -> pokemonService.deletePokemonId(pokemonId));
    }

    // my tests cases

    @Test
    public void PokemonService_GetAllPokemon_2() {

        // We are testing the GetAllPokemon method of the service class
        // we have to test the behavior of it but for it we first have to
        // mock all the classes and the objects that service method depends on
        // it is not required to mock the Pageable class as it would just be passed as
        // an argument to the method

        PokemonResponse pokemonReturn = Mockito.mock(PokemonResponse.class);
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        // Mockito.any specify that the method can accept any value of the given type
        // which
        // here is Pageable class and we are passing the mock object of Page class to it
        // we are stubbing the behavior of findAll method , mocking refers to
        // creating mock object while stubbing refers to defining the behavior of
        // the mocked object.
        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        // test the behavior of the method
        PokemonResponse savePokemon = pokemonService.getAllPokemon(1, 10);

        // Assertions

        Assertions.assertThat(savePokemon).isNotNull();

    }

    @Test
    public void PokemonService_GetPokemonByIdTest() {
        // mock all the dependencies
        // we have repo , we dont need anything
        // we have to check or test the behaviour of the method

        Pokemon pokemon = Pokemon.builder().name("Pikachu")
                .type("electric")
                .build();

        // we have to mock the behavior of the findbyId method , because service
        // will call this method so we stub the behavior of this method
        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));

        // actually testing the method
        PokemonDto pokemonTest = pokemonService.getPokemonById(1);

        Assertions.assertThat(pokemonTest).isNotNull();
    }

    @Test
    public void PokemonService_UpdatePokemonTest() {
        // findbyid first - stub this method
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder().name("Pikachu").type("Electric").build();
        PokemonDto pokemonDTO = PokemonDto.builder().name("pikachu").type("electric").build();

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        when(pokemonRepository.save(pokemon)).thenReturn(pokemon);

        PokemonDto pokDTO = pokemonService.updatePokemon(pokemonDTO, pokemonId);

        Assertions.assertThat(pokDTO).isNotNull();
        Assertions.assertThat(pokDTO.getId()).isNotNull();
    }

    @Test
    public void PokemonService_DeletePokemonTest() {
        // repository mock fist
        // add to the repo first , then stub
        int pokemonId = 1;
        Pokemon pokemon = Pokemon.builder().name("Pikachu").type("Electric").build();
        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        when(pokemonRepository.save(pokemon)).thenReturn(pokemon);
        doNothing().when(pokemonRepository).delete(pokemon);
        
        assertAll(() -> {
            pokemonService.deletePokemonId(pokemonId);
        });
    }
}
