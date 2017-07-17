package example.com.marvel.domain.usercases.impl;


import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.GetFavoriteMarvelCharactersUserCase;
import example.com.marvel.domain.usercases.base.AbstractUserCase;

/**
 * User case implementation that retrieves a list of favorite marvel characters.
 */
public class GetFavoriteMarvelCharactersUserCaseImpl extends AbstractUserCase implements GetFavoriteMarvelCharactersUserCase {

    private MarvelRepository repository;
    private Callback getFavoriteCharactersCallback;

    public GetFavoriteMarvelCharactersUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                                   MarvelRepository repository, Callback callback) {
        super(threadExecutor, mainThread);
        this.repository = repository;
        this.getFavoriteCharactersCallback = callback;
    }


    @Override
    public void run() {
        // retrieve to the main thread the favorite characters.
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                getFavoriteCharactersCallback.
                        onFavoriteMarvelCharactersRetrieved(repository.getFavoriteMarvelCharacters());
            }
        });
    }
}
