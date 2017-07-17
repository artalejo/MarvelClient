package example.com.marvel.presentation.presenters;


import example.com.marvel.domain.executor.Executor;
import example.com.marvel.domain.executor.MainThread;

public abstract class AbstractPresenter {

    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
