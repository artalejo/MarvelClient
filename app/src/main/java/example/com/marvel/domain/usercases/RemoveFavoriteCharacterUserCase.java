package example.com.marvel.domain.usercases;


import example.com.marvel.domain.usercases.base.UserCase;

/**
 * User case interface that removes a character from its favorites.
 */
public interface RemoveFavoriteCharacterUserCase extends UserCase {

    interface Callback {
        void onFavoriteCharacterRemoved(long characterId);
    }
}
