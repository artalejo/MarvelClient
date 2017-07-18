package example.com.marvel.domain.usercases.impl;


import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.RemoveFavoriteCharacterUserCase;
import example.com.marvel.domain.usercases.base.AbstractUserCase;
import example.com.marvel.network.models.MarvelCharacter;

/**
 * User case that removes a favorite character.
 */
public class RemoveFavoriteCharacterUserCaseImpl extends AbstractUserCase implements RemoveFavoriteCharacterUserCase {

    private final MarvelCharacter character;
    private final boolean isFavorite;
    private MarvelRepository repository;
    private Callback removeFavoriteCharacterCallback;

    public RemoveFavoriteCharacterUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                               MarvelRepository repository, MarvelCharacter character,
                                               boolean isFavorite, Callback callback) {

        super(threadExecutor, mainThread);
        this.removeFavoriteCharacterCallback = callback;
        this.character = character;
        this.isFavorite = isFavorite;
        this.repository = repository;
    }


    @Override
    public void run() {

        final int isCharacterFavorite = repository.setCharacterFavoriteStatus(character, isFavorite);
        final long removedCharacterId = isCharacterFavorite == 0 ? character.getId() : -1;

        // notify on the main thread that we have removed the character from the favorites list.
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                removeFavoriteCharacterCallback.onFavoriteCharacterRemoved(removedCharacterId);
            }
        });

    }
}
