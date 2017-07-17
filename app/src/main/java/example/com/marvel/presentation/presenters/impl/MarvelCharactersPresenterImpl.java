package example.com.marvel.presentation.presenters.impl;

import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.GetMarvelCharactersUserCase;
import example.com.marvel.domain.usercases.SetCharacterFavoriteStatusUserCase;
import example.com.marvel.domain.usercases.impl.GetMarvelCharactersUserCaseImpl;
import example.com.marvel.domain.usercases.impl.SetCharacterFavoriteStatusUserCaseImpl;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.network.models.MarvelCharactersResponse;
import example.com.marvel.presentation.presenters.AbstractPresenter;
import example.com.marvel.presentation.presenters.MarvelCharactersPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Presenter implementation that handles the marvel character actions.
 */

public class MarvelCharactersPresenterImpl extends AbstractPresenter
        implements MarvelCharactersPresenter, SetCharacterFavoriteStatusUserCase.Callback {


    private MarvelRepository repository;
    private MarvelCharactersPresenter.View mView;
    private final Callback<MarvelCharactersResponse> getMarvelCharactersCallback;

    public MarvelCharactersPresenterImpl(Executor executor, MainThread mainThread,
                                         MarvelCharactersPresenter.View view, MarvelRepository repository) {

        super(executor, mainThread);
        this.mView = view;
        this.repository = repository;
        this.getMarvelCharactersCallback = getMarvelCharactersCallBack();

    }

    // USER CASES

    @Override
    public void showMarvelCharacters(long charactersOffset) {
        GetMarvelCharactersUserCase getMarvelCharactersUserCase =
                new GetMarvelCharactersUserCaseImpl(mExecutor, mMainThread,
                                                    repository, charactersOffset,
                                                    getMarvelCharactersCallback);
        getMarvelCharactersUserCase.execute();
    }

    @Override
    public void setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite) {
        SetCharacterFavoriteStatusUserCase setCharacterFavoriteStatusUserCase =
                new SetCharacterFavoriteStatusUserCaseImpl(mExecutor, mMainThread, repository,
                                                           character, isFavorite, this);
        setCharacterFavoriteStatusUserCase.execute();
    }

    // VIEW CALLS

    @Override
    public void onCharacterFavoriteStatusSet(int isFavorite) {
        mView.onCharacterFavoriteStatusSet(isFavorite);
    }

    // MARVEL API CALLBACKS

    private Callback<MarvelCharactersResponse> getMarvelCharactersCallBack() {
        return new Callback<MarvelCharactersResponse>() {
            @Override
            public void onResponse(Call<MarvelCharactersResponse> call, Response<MarvelCharactersResponse> response) {
                if (response.isSuccessful()) {
                    MarvelCharactersResponse marvelCharactersResponse = response.body();
                    mView.onCharactersRetrieved(marvelCharactersResponse.getData().getResults());
                }
                else {
                    mView.onCharactersRetrieved(null);
                }
            }

            @Override
            public void onFailure(Call<MarvelCharactersResponse> call, Throwable t) {
                mView.onCharactersRetrieved(null);
            }
        };
    }

}
