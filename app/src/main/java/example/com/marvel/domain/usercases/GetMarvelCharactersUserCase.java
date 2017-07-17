package example.com.marvel.domain.usercases;


import example.com.marvel.domain.usercases.base.UserCase;

/**
 * User case interface that retrieves a list of marvel characters.
 */
public interface GetMarvelCharactersUserCase extends UserCase {

    interface Callback {
        void onMarvelCharactersRetrieved();
    }
}
