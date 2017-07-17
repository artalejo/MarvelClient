package example.com.marvel.presentation.presenters.impl;

import java.util.List;

import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.GetFavoriteMarvelCharactersUserCase;
import example.com.marvel.domain.usercases.RemoveFavoriteCharacterUserCase;
import example.com.marvel.domain.usercases.impl.GetFavoriteMarvelCharactersUserCaseImpl;
import example.com.marvel.domain.usercases.impl.RemoveFavoriteCharacterUserCaseImpl;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.presentation.presenters.AbstractPresenter;
import example.com.marvel.presentation.presenters.FavoriteCharactersPresenter;

/**
 * Presenter implementation that handles the favorite actions of the Marvel Characters.
 */

public class FavoriteCharactersPresenterImpl extends AbstractPresenter
                                             implements FavoriteCharactersPresenter,
                                             GetFavoriteMarvelCharactersUserCase.Callback, RemoveFavoriteCharacterUserCase.Callback {

    private MarvelRepository repository;
    private View mView;

    public FavoriteCharactersPresenterImpl(Executor executor, MainThread mainThread,
                                           View view, MarvelRepository repository) {
        super(executor, mainThread);
        this.mView = view;
        this.repository = repository;
    }

    // USER CASES

    @Override
    public void showFavoriteMarvelCharacters() {
        GetFavoriteMarvelCharactersUserCase getMarvelCharactersUserCase =
                new GetFavoriteMarvelCharactersUserCaseImpl(mExecutor, mMainThread, repository, this);
        getMarvelCharactersUserCase.execute();
    }

    @Override
    public void removeCharacterFromFavorites(MarvelCharacter character, boolean isFavorite) {
        RemoveFavoriteCharacterUserCase removeCharacterFromFavoritesUserCase =
                new RemoveFavoriteCharacterUserCaseImpl(mExecutor, mMainThread, repository,
                                                        character, isFavorite, this);
        removeCharacterFromFavoritesUserCase.execute();
    }

    // VIEW CALLS
    @Override
    public void onFavoriteMarvelCharactersRetrieved(List<MarvelCharacter> favoriteCharacters) {
        mView.onFavoriteCharactersRetrieved(favoriteCharacters);
    }

    @Override
    public void onFavoriteCharacterRemoved(long characterId) {
        mView.onCharacterRemovedFromFavorite(characterId);
    }
}
