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
import example.com.marvel.domain.usercases.impl.SetCharacterFavoriteStatusUserCaseImpl;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.storage.database.DbHelper;
import example.com.marvel.threading.TestMainThread;

import static org.mockito.Mockito.times;


public class SetCharacterFavoriteStatusTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private SetCharacterFavoriteStatusUserCase.Callback mockedCallback;
    @Mock private MarvelCharacter mockedCharacter;
    @Mock private Context mockedContext;
    @Mock private MarvelRepository mockedRepo;
    @Mock private DbHelper mockedDb;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void notAddingFavoriteCharacterWhenCharacterIsNullTest() throws Exception {
        SetCharacterFavoriteStatusUserCaseImpl userCase =
                new SetCharacterFavoriteStatusUserCaseImpl(mExecutor, mMainThread, mockedRepo,
                                                          null, true, mockedCallback);
        userCase.run();

        Mockito.verify(mockedCallback).onCharacterFavoriteStatusSet(0);
        Mockito.verifyNoMoreInteractions(mockedCallback);
    }

    @Test
    public void addingFavoriteCharacterWhenCharacterNotNullTest() throws Exception {
        SetCharacterFavoriteStatusUserCaseImpl userCase =
                new SetCharacterFavoriteStatusUserCaseImpl(mExecutor, mMainThread, mockedRepo,
                                                           mockedCharacter, true, mockedCallback);
        userCase.run();

        Mockito.verify(mockedRepo).setCharacterFavoriteStatus(mockedCharacter, true);
        Mockito.verifyNoMoreInteractions(mockedRepo);
        Mockito.verify(mockedCallback, times(1)).onCharacterFavoriteStatusSet(Mockito.anyInt());
    }
}
