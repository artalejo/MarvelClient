package example.com.marvel.presentation.presenters;

import java.util.List;

import example.com.marvel.network.models.MarvelCharacter;

/**
 * Presenter interface that handles data from and to the Marvel Characters activity.
 */
public interface MarvelCharactersPresenter {

    interface View {
        void onCharactersRetrieved(List<MarvelCharacter> characters);
        void onCharacterFavoriteStatusSet(int favoriteStatus);
    }

    void showMarvelCharacters(long charactersOffset);
    void setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite);
}
