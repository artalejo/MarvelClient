package example.com.marvel.domain.usercases;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.impl.GetFavoriteMarvelCharactersUserCaseImpl;
import example.com.marvel.domain.usercases.impl.SetCharacterFavoriteStatusUserCaseImpl;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.storage.database.DbHelper;
import example.com.marvel.threading.TestMainThread;

public class GetFavoriteMarvelCharactersTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private SetCharacterFavoriteStatusUserCase.Callback mockedSetCharacterFavoriteStatusCallback;
    @Mock private GetFavoriteMarvelCharactersUserCase.Callback mockedGeMarvelCharactersCallback;
    @Mock private MarvelCharacter mockedMarvelCharacter;
    @Mock private Context mockedContext;
    @Mock private MarvelRepository mockedRepo;
    @Mock private DbHelper mockedDb;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void getFavoritesMarvelCharactersTest() throws Exception {
        GetFavoriteMarvelCharactersUserCaseImpl userCase =
                new GetFavoriteMarvelCharactersUserCaseImpl(mExecutor, mMainThread,
                        mockedRepo, mockedGeMarvelCharactersCallback);
        userCase.run();

        Mockito.verify(mockedGeMarvelCharactersCallback).onFavoriteMarvelCharactersRetrieved(Matchers.anyListOf(MarvelCharacter.class));
        Mockito.verifyNoMoreInteractions(mockedSetCharacterFavoriteStatusCallback);
    }

    @Test
    public void getFavoriteMarvelCharacterJustAddedTest() throws Exception {
        // Adding a character
        SetCharacterFavoriteStatusUserCaseImpl addCharactersUserCase =
                new SetCharacterFavoriteStatusUserCaseImpl(mExecutor, mMainThread, mockedRepo,
                        mockedMarvelCharacter, true, mockedSetCharacterFavoriteStatusCallback);
        addCharactersUserCase.run();

        GetFavoriteMarvelCharactersUserCaseImpl userCase =
                new GetFavoriteMarvelCharactersUserCaseImpl(mExecutor, mMainThread,
                                                            mockedRepo, mockedGeMarvelCharactersCallback);
        userCase.run();

        Mockito.verify(mockedSetCharacterFavoriteStatusCallback).onCharacterFavoriteStatusSet(0);
        Mockito.verify(mockedGeMarvelCharactersCallback).onFavoriteMarvelCharactersRetrieved(Matchers.anyListOf(MarvelCharacter.class));
        Mockito.verifyNoMoreInteractions(mockedSetCharacterFavoriteStatusCallback);
    }
}
