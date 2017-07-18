package example.com.marvel.domain.repository;

import java.util.HashSet;
import java.util.List;

import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.network.models.MarvelCharactersResponse;
import retrofit2.Callback;

/**
 * Interface that abstracts the way of retrieving the info about the marvel characters.
 */

public interface MarvelRepository {

    void getMarvelCharacters(long charactersOffset, Callback<MarvelCharactersResponse> callback);
    List<MarvelCharacter> getFavoriteMarvelCharacters();
    HashSet<Long> getFavoriteMarvelCharactersIds();
    int setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite);

}
