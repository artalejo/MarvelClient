package example.com.marvel.domain.usercases.impl;


import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.SetCharacterFavoriteStatusUserCase;
import example.com.marvel.domain.usercases.base.AbstractUserCase;
import example.com.marvel.network.models.MarvelCharacter;

/**
 * User case that sets the character favorite status.
 */
public class SetCharacterFavoriteStatusUserCaseImpl extends AbstractUserCase implements SetCharacterFavoriteStatusUserCase {

    private final MarvelCharacter character;
    private final boolean isFavorite;
    private MarvelRepository repository;
    private Callback setFavoriteStatusCallback;

    public SetCharacterFavoriteStatusUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                                  MarvelRepository repository, MarvelCharacter character,
                                                  boolean isFavorite, Callback callback) {

        super(threadExecutor, mainThread);
        this.setFavoriteStatusCallback = callback;
        this.character = character;
        this.isFavorite = isFavorite;
        this.repository = repository;
    }


    @Override
    public void run() {

        final int isCharacterFavorite =
                repository.setCharacterFavoriteStatus(character, isFavorite);
        // notify on the main thread that we have inserted the character favorite status.
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                setFavoriteStatusCallback.onCharacterFavoriteStatusSet(isCharacterFavorite);
            }
        });

    }
}
