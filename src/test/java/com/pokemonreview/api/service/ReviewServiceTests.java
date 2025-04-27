package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;

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

    @Test
    public void ReviewService_CreateReview_ReturnsReviewDto() {
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(pokemon.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewsByPokemonId_ReturnReviewDto() {
        int reviewId = 1;
        when(reviewRepository.findByPokemonId(reviewId)).thenReturn(Arrays.asList(review));

        List<ReviewDto> pokemonReturn = reviewService.getReviewsByPokemonId(reviewId);

        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewById_ReturnReviewDto() {
        int reviewId = 1;
        int pokemonId = 1;

        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewDto reviewReturn = reviewService.getReviewById(reviewId, pokemonId);

        Assertions.assertThat(reviewReturn).isNotNull();
        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_UpdatePokemon_ReturnReviewDto() {
        int pokemonId = 1;
        int reviewId = 1;
        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto updateReturn = reviewService.updateReview(pokemonId, reviewId, reviewDto);

        Assertions.assertThat(updateReturn).isNotNull();
    }

    @Test
    public void ReviewService_DeletePokemonById_ReturnVoid() {
        int pokemonId = 1;
        int reviewId = 1;

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertAll(() -> reviewService.deleteReview(pokemonId, reviewId));
    }

    // my tests

    /*
     * public void init() {
     * pokemon = Pokemon.builder().name("pikachu").type("electric").build();
     * pokemonDto = PokemonDto.builder().name("pickachu").type("electric").build();
     * review = Review.builder().title("title").content("content").stars(5).build();
     * reviewDto =
     * ReviewDto.builder().title("review title").content("test content").stars(5).
     * build();
     * }
     */

    @Test
    public void ReviewService_createReviewTest() {
        // create review
        // find the pokemon by Id
        // set the review
        // save the review in the repo
        // map to the review dto to return back
        int pokemonId = 1;

        /*
         * Mockito.any is argument matcher or a way of stubbing it is used when 
         * any value of certain type can be used as an argument to method call on a mock
         * object.
         * Here mock object is reviewRepo and pokemonRepo, in this example any value will be 
         * any value having a class of type Review.class
         */

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);
        ReviewDto dto = reviewService.createReview(pokemonId, reviewDto);

        Assertions.assertThat(dto).isNotNull();
    }

    @Test
    public void ReviewService_GetReviewByPokemonId() {
        int pokemonId = 1;

        when(reviewRepository.findByPokemonId(pokemonId)).thenReturn(Arrays.asList(review));
        List<ReviewDto> reviews = reviewService.getReviewsByPokemonId(pokemonId);

        Assertions.assertThat(reviews).isNotNull();
    }

   @Test
   public void ReviewService_GetReviewById(){
    int reviewId = 1;
    int pokemonId = 1;
    review.setPokemon(pokemon); // because there is a check in the service layer that the review
    // belongs to the pokemon and therefore we need to set the review with the pokemon 

    // get pokemon by the id , get the review by id
    // if pokemonid and reviewid is equal then  great otherwise review is not there 
    // map review to reviewDTO and return it to the client 

    when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));
    when (reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));

    ReviewDto resultReviewDto =  reviewService.getReviewById(reviewId, pokemonId);

    Assertions.assertThat(resultReviewDto).isNotNull();

   } 

   @Test
   public void ReviewService_UpdateReview_ReturnsReviewDTO(){
        int reviewId = 1 , pokemonId = 1;
        // we have reviewDto 
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto resultDTOResult = reviewService.updateReview(pokemonId, reviewId, reviewDto);

        Assertions.assertThat(resultDTOResult).isNotNull();
   }

   @Test
   public void ReviewService_DeleteReview_ReturnsNothing(){
        review.setPokemon(pokemon);
        pokemon.setReviews(List.of(review));

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(1)).thenReturn(Optional.ofNullable(review));
        
        assertAll(() -> reviewService.deleteReview(1,1));
   }

   @Test
   public void ReviewService_DeleteReview_ReturnsNothing_TestCase2(){
        review.setPokemon(pokemon);
        pokemon.setReviews(List.of(review));

        when(pokemonRepository.findById(1)).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(1)).thenReturn(Optional.ofNullable(review));
        
       reviewService.deleteReview(1, 1);

       Optional<Review> deletedEntity = reviewRepository.findById(1);
       Assertions.assertThat(deletedEntity).isEmpty();
   }

}
