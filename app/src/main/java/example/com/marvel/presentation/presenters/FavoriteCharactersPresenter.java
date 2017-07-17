package example.com.marvel.presentation.presenters;

import java.util.List;

import example.com.marvel.network.models.MarvelCharacter;

/**
 * Presenter interface that handles the favorite actions of the Marvel Characters.
 */
public interface FavoriteCharactersPresenter {

    interface View {
        void onFavoriteCharactersRetrieved(List<MarvelCharacter> characters);
        void onCharacterRemovedFromFavorite(long characterId);
    }

    void showFavoriteMarvelCharacters();
    void removeCharacterFromFavorites(MarvelCharacter character, boolean isFavorite);
}
