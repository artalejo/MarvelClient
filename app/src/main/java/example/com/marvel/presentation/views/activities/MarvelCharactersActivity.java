package example.com.marvel.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.marvel.R;
import example.com.marvel.domain.executor.impl.ThreadExecutor;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.presentation.presenters.MarvelCharactersPresenter;
import example.com.marvel.presentation.presenters.impl.MarvelCharactersPresenterImpl;
import example.com.marvel.presentation.views.adapters.MarvelCharactersAdapter;
import example.com.marvel.presentation.views.listeners.EndlessRecyclerOnScrollListener;
import example.com.marvel.storage.MarvelRepositoryImpl;
import example.com.marvel.threading.MainThreadImpl;

import static example.com.marvel.Constants.MARVEL_CHARACTER;
import static example.com.marvel.domain.Utils.getFavoriteStatusMsgID;
import static example.com.marvel.network.services.MarvelServices.CHARACTERS_REQUEST_LIMIT;

/**
 * Marvel Activity shows the marvel allCharacters.
 */

public class MarvelCharactersActivity extends AppCompatActivity
        implements MarvelCharactersAdapter.CharacterFavoriteActionCallback, MarvelCharactersPresenter.View {

    @BindView(R.id.marvel_characters_toolbar) Toolbar marvelCharsToolbar;
    @BindView(R.id.marvel_characters_recycler) RecyclerView marvelCharsRecycler;
    @BindView(R.id.marvel_characters_progress_linear) LinearLayout marvelCharsProgressBar;

    private MarvelCharactersPresenterImpl marvelCharactersPresenter;
    private MarvelCharactersAdapter charactersAdapter;
    private MarvelRepository marvelRepository;
    private long CHARACTERS_OFFSET = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marvel_characters_activity);
        ButterKnife.bind(this);
        marvelRepository = new MarvelRepositoryImpl(this);
        init();
    }

    private void init() {

        if (marvelCharsToolbar != null) {
            marvelCharsToolbar.setTitle(R.string.marvel_characters_activity_toolbar_title);
            setSupportActionBar(marvelCharsToolbar);
        }

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        marvelCharsRecycler.setLayoutManager(layoutManager);
        marvelCharsRecycler.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                charactersAdapter.addLoadingView();
                fetchNewMarvelCharacters();
            }

        });

        charactersAdapter = new MarvelCharactersAdapter(this, marvelRepository, this);

        marvelCharsRecycler.setAdapter(charactersAdapter);

        marvelCharactersPresenter = new MarvelCharactersPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this, new MarvelRepositoryImpl(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.charactersAdapter != null && this.charactersAdapter.getItemCount() == 0) {
            fetchNewMarvelCharacters();
            marvelCharsProgressBar.setVisibility(View.VISIBLE);
            marvelCharsRecycler.setVisibility(View.GONE);
        }
        else if (this.charactersAdapter != null) {
            charactersAdapter.refreshMarvelCharactersAdapter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.marvel_characters_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.favorite_characters:
                startFavoriteCharactersActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Removing all activities when moving to the back
        ActivityCompat.finishAffinity(this);
        moveTaskToBack(true);
    }

    private void fetchNewMarvelCharacters() {
        marvelCharactersPresenter.showMarvelCharacters(CHARACTERS_OFFSET);
        CHARACTERS_OFFSET += CHARACTERS_REQUEST_LIMIT;
    }

    private void startFavoriteCharactersActivity() {
        Intent favoriteCharactersIntent = new Intent(MarvelCharactersActivity.this, FavoriteMarvelCharactersActivity.class);
        startActivity(favoriteCharactersIntent);
    }

    // ADAPTER CALLBACK
    @Override
    public void setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite) {
        marvelCharactersPresenter.setCharacterFavoriteStatus(character, isFavorite);
    }

    @Override
    public void showCharacterDetail(MarvelCharacter character) {
        Intent showCharacterDetailIntent = new Intent(MarvelCharactersActivity.this,
                                                      MarvelCharacterDetailActivity.class);

        showCharacterDetailIntent.putExtra(MARVEL_CHARACTER, character);
        startActivity(showCharacterDetailIntent);
    }

    // PRESENTER CALLBACKS

    @Override
    public void onCharactersRetrieved(List<MarvelCharacter> newCharacters) {
        if (newCharacters != null) {
            charactersAdapter.updateMarvelCharactersAdapter(newCharacters);
            marvelCharsRecycler.setVisibility(View.VISIBLE);
            marvelCharsProgressBar.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, R.string.marvel_new_characters_request_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCharacterFavoriteStatusSet(int favoriteStatus) {
        int msg = getFavoriteStatusMsgID(favoriteStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
