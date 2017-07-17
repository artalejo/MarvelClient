package example.com.marvel.domain.usercases;


import example.com.marvel.domain.usercases.base.UserCase;

/**
 * User case interface that sets the favorite status of a marvel character.
 */
public interface SetCharacterFavoriteStatusUserCase extends UserCase {

    interface Callback {
        void onCharacterFavoriteStatusSet(int isFavorite);
    }
}
