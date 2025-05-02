package com.pokemonreview.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.repository.TestRepoPokemon;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrlString = "http://localhost";

	private static TestRestTemplate restTemplate;

	@Autowired
	private TestRepoPokemon pokemonRepo;

	@BeforeAll
	public static void init() {
		restTemplate = new TestRestTemplate();
	}

	@BeforeEach
	public void setup() {
		baseUrlString = baseUrlString.concat(":").concat(port + "").concat("/api");
	}

	@Test
	public void testAddPokemon() {
		// it needs pokemonDto object so we create it
		PokemonDto pokemonDto = PokemonDto.builder().name("Pikachu").type("electric").build();
		PokemonDto productdto = restTemplate.postForObject(baseUrlString + "/pokemon/create", pokemonDto,
				PokemonDto.class);
		assertEquals("Pikachu", productdto.getName());
		assertEquals(1, pokemonRepo.findAll().size());
	}

	@Test
	@Sql(statements = "Insert into POKEMON (id , name , type) values (1 , 'Pikachu' , 'Electric')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "Delete from POKEMON_DTO  where name = 'Pikachu'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // for
																																// cleanup
	public void testGetPokemon() throws Exception {
		List<PokemonDto> pokemons = restTemplate.getForObject(baseUrlString, List.class);
		// assert to check size, first compare it with the products size and then
		// compare it with the repos size
		// here we are returing PokemonResponse in response entity
		// PokemonResponse has List feild that contains the List of Pokeon DTOs , page
		// size , page no , totalElements , totalPages , last
		// getAllPokemons method in the Controller takes two paramenters two , pageNo ,
		// PageSize
		int pageNo = 0, pageSize = 10;

		// Here we can omit the SQL Query that wer have used on this test since we are
		// anyways adding the Object
		PokemonResponse pokemonResponse = PokemonResponse.builder()
				.pageNo(0)
				.pageSize(10)
				.last(true)
				.content(Arrays.asList(
						PokemonDto.builder().name("Pikachu").type("Electric").build(),
						PokemonDto.builder().name("Charmander").type("Fire").build()))
				.build();

		ResponseEntity<PokemonResponse> response = restTemplate.getForEntity(
				baseUrlString + "/pokemon" + "?pageNo=" + pageNo + "&pageSize" + pageSize, PokemonResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getContent()).hasSize(2);
		Assertions.assertThat(response.getBody().getContent().get(0).getName()).isEqualTo("Pikachu");

		assertAll(
			() -> assertEquals(response.getStatusCode(), HttpStatus.OK),
			() -> assertEquals(response.getBody().getContent().size(), 2));

	}

}
