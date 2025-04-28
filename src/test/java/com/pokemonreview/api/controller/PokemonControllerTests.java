package com.pokemonreview.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTests {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PokemonService pokemonService;

        @Autowired
        private ObjectMapper objectMapper;
        private Pokemon pokemon;
        private Review review;
        private ReviewDto reviewDto;
        private PokemonDto pokemonDto;

        @BeforeEach
        public void init() {
                pokemon = Pokemon.builder().name("pikachu").type("electric").build();
                pokemonDto = PokemonDto.builder().name("pickachu").type("electric").build();
                review = Review.builder().title("title").content("content").stars(5).build();
                reviewDto = ReviewDto.builder().title("review title").content("test content").stars(5).build();
        }

        // @Test
        // public void PokemonController_CreatePokemon_ReturnCreated() throws Exception
        // {
        // given(pokemonService.createPokemon(ArgumentMatchers.any()))
        // .willAnswer((invocation -> invocation.getArgument(0)));

        // ResultActions response = mockMvc.perform(post("/api/pokemon/create")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(pokemonDto)));

        // response.andExpect(MockMvcResultMatchers.status().isCreated())
        // .andExpect(MockMvcResultMatchers.jsonPath("$.name",
        // CoreMatchers.is(pokemonDto.getName())))
        // .andExpect(MockMvcResultMatchers.jsonPath("$.type",
        // CoreMatchers.is(pokemonDto.getType())));
        // }

        @Test
        public void PokemonController_GetAllPokemon_ReturnResponseDto() throws Exception {
                PokemonResponse responseDto = PokemonResponse.builder().pageSize(10).last(true).pageNo(1)
                                .content(Arrays.asList(pokemonDto)).build();
                when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);

                ResultActions response = mockMvc.perform(get("/api/pokemon")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNo", "1")
                                .param("pageSize", "10"));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()",
                                                CoreMatchers.is(responseDto.getContent().size())));
        }

        @Test
        public void PokemonController_PokemonDetail_ReturnPokemonDto() throws Exception {
                int pokemonId = 1;
                when(pokemonService.getPokemonById(pokemonId)).thenReturn(pokemonDto);

                ResultActions response = mockMvc.perform(get("/api/pokemon/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pokemonDto)));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                CoreMatchers.is(pokemonDto.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.type",
                                                CoreMatchers.is(pokemonDto.getType())));
        }

        @Test
        public void PokemonController_UpdatePokemon_ReturnPokemonDto() throws Exception {
                int pokemonId = 1;
                when(pokemonService.updatePokemon(pokemonDto, pokemonId)).thenReturn(pokemonDto);

                ResultActions response = mockMvc.perform(put("/api/pokemon/1/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pokemonDto)));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                CoreMatchers.is(pokemonDto.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.type",
                                                CoreMatchers.is(pokemonDto.getType())));
        }

        @Test
        public void PokemonController_DeletePokemon_ReturnString() throws Exception {
                int pokemonId = 1;
                doNothing().when(pokemonService).deletePokemonId(1);

                ResultActions response = mockMvc.perform(delete("/api/pokemon/1/delete")
                                .contentType(MediaType.APPLICATION_JSON));

                response.andExpect(MockMvcResultMatchers.status().isOk());
        }

        /*
         * @Autowired
         * private MockMvc mockMvc;
         * 
         * @MockBean
         * private PokemonService pokemonService;
         * 
         * @Autowired
         * private ObjectMapper objectMapper;
         * private Pokemon pokemon;
         * private Review review;
         * private ReviewDto reviewDto;
         * private PokemonDto pokemonDto;
         * 
         * @BeforeEach
         * public void init() {
         * pokemon = Pokemon.builder().name("pikachu").type("electric").build();
         * pokemonDto = PokemonDto.builder().name("pickachu").type("electric").build();
         * review = Review.builder().title("title").content("content").stars(5).build();
         * reviewDto =
         * ReviewDto.builder().title("review title").content("test content").stars(5).
         * build();
         * }
         */

        // my tests
        @Test
        public void PokemonController_CreatePokemon_ReturnsCreated() throws JsonProcessingException, Exception {
                given(pokemonService.createPokemon(ArgumentMatchers.any()))
                                .willAnswer(invocation -> invocation.getArgument(0));

                ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pokemonDto)));

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$name",
                                                CoreMatchers.is(pokemonDto.getName())))
                                .andDo(MockMvcResultHandlers.print());
                // here we are using pokemonDto to send the data in post request
                // by converting it to the json
                // refer to notes too
                // end

        }

        @Test
        public void PokemonController_getAllPokemons_ReturnResponseDTO() throws Exception {
                PokemonResponse responseDto = PokemonResponse.builder().pageSize(10).last(true).pageNo(1)
                                .content(Arrays.asList(pokemonDto)).build();
                when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);

                // we can see that the request has the parameters which we need to add in
                // this api call - test call
                ResultActions response = mockMvc.perform(get("/api/pokemon")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNo", "1")
                                .param("pageSize", "10"));
                response.andExpect(MockMvcResultMatchers.status().isOk())
                                // we want to make sure that we are getting the content back in it
                                // too
                                .andExpect(MockMvcResultMatchers.jsonPath("$content.size",
                                                CoreMatchers.is(responseDto.getContent().size())));

        }

        @Test
        public void PokemonController_GetPokemonDetail_ReturnsResponseEntity() throws Exception {
                // it takes pathvariable as the input
                // Act , first i have to mock the service layer that is the service call from
                // the controller itsef
                int pokemonId = 1;
                when(pokemonService.getPokemonById(1)).thenReturn(pokemonDto);

                ResultActions response = mockMvc.perform(get("/api/pokemon/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pokemonDto)));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                CoreMatchers.is(pokemonDto.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.type",
                                                CoreMatchers.is(pokemonDto.getType())));
        }

        @Test
        public void PokemonController_UpdatePokemonDetail_ReturnsResponseEntity() throws Exception {
                int pokemonId = 1;
                // we are stubbing the pokemonService call and returning the same pokemonDto
                // back that we supplied
                when(pokemonService.updatePokemon(pokemonDto, pokemonId)).thenReturn(pokemonDto);
                // given(pokemonService.updatePokemon(pokemonDto, pokemonId)).willAnswer(inv ->
                // inv.getArgument(0));

                // Act
                ResultActions response = mockMvc.perform(put("/api/pokemon/1/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pokemonDto))

                );

                response.andExpect(MockMvcResultMatchers.status().isOk());
        }
}
