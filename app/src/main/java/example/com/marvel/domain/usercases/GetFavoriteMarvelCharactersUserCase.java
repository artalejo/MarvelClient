package example.com.marvel.domain.usercases;


import java.util.List;

import example.com.marvel.domain.usercases.base.UserCase;
import example.com.marvel.network.models.MarvelCharacter;

/**
 * User case interface that retrieves a list of favorite marvel characters.
 */
public interface GetFavoriteMarvelCharactersUserCase extends UserCase {

    interface Callback {
        void onFavoriteMarvelCharactersRetrieved(List<MarvelCharacter> favoriteCharacters);
    }
}
