package example.com.marvel.domain.usercases;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.domain.usercases.impl.RemoveFavoriteCharacterUserCaseImpl;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.threading.TestMainThread;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


public class RemoveFavoriteCharacterTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private RemoveFavoriteCharacterUserCase.Callback mockedCallback;
    @Mock private MarvelCharacter mockedMarvelCharacter;
    @Mock private Context mockedContext;
    @Mock private MarvelRepository mockedRepo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
        when(mockedMarvelCharacter.getId()).thenReturn(22l);
    }

    @Test
    public void removingFavoriteCharacterWhenCharacterNotNullTest() throws Exception {
        RemoveFavoriteCharacterUserCaseImpl userCase =
                new RemoveFavoriteCharacterUserCaseImpl(mExecutor, mMainThread, mockedRepo,
                                                        mockedMarvelCharacter, true, mockedCallback);
        userCase.run();

        Mockito.verify(mockedRepo).setCharacterFavoriteStatus(mockedMarvelCharacter, true);
        Mockito.verifyNoMoreInteractions(mockedRepo);
        Mockito.verify(mockedCallback, times(1)).onFavoriteCharacterRemoved(22l);
    }
}
