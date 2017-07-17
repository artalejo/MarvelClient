package example.com.marvel.domain.usercases.impl;


import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.GetMarvelCharactersUserCase;
import example.com.marvel.domain.usercases.base.AbstractUserCase;
import example.com.marvel.network.models.MarvelCharactersResponse;

/**
 * User case implementation that retrieves a list of marvel characters.
 */
public class GetMarvelCharactersUserCaseImpl extends AbstractUserCase implements GetMarvelCharactersUserCase {

    private final long charactersOffset;
    private MarvelRepository repository;
    private retrofit2.Callback<MarvelCharactersResponse> getMarvelCharactersCallback;

    public GetMarvelCharactersUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                           MarvelRepository repository, long charactersOffset,
                                           retrofit2.Callback<MarvelCharactersResponse> callback) {

        super(threadExecutor, mainThread);
        this.getMarvelCharactersCallback = callback;
        this.charactersOffset = charactersOffset;
        this.repository = repository;
    }


    @Override
    public void run() {
        repository.getMarvelCharacters(charactersOffset, getMarvelCharactersCallback);
    }
}
