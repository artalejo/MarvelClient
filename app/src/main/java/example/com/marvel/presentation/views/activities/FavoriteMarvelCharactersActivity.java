package example.com.marvel.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.marvel.R;
import example.com.marvel.domain.executor.impl.ThreadExecutor;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.presentation.presenters.FavoriteCharactersPresenter;
import example.com.marvel.presentation.presenters.impl.FavoriteCharactersPresenterImpl;
import example.com.marvel.presentation.views.adapters.MarvelCharactersAdapter;
import example.com.marvel.storage.MarvelRepositoryImpl;
import example.com.marvel.threading.MainThreadImpl;

import static example.com.marvel.Constants.MARVEL_CHARACTER;

/**
 * Activity that displays the favorite marvel characters.
 */

public class FavoriteMarvelCharactersActivity extends AppCompatActivity
        implements MarvelCharactersAdapter.CharacterFavoriteActionCallback, FavoriteCharactersPresenter.View {

    @BindView(R.id.favorite_characters_toolbar) Toolbar favCharactersToolbar;
    @BindView(R.id.favorite_characters_recycler) RecyclerView favCharactersRecycler;
    @BindView(R.id.favorite_characters_progress_linear) LinearLayout favCharactersProgressBar;
    @BindView(R.id.empty_favorite_characters) LinearLayout emptyCharactersLinear;

    private List<MarvelCharacter> favoriteCharacters;
    private MarvelCharactersAdapter charactersAdapter;
    private FavoriteCharactersPresenterImpl favoriteCharactersPresenter;
    private MarvelRepository marvelRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_characters_activity);
        ButterKnife.bind(this);
        marvelRepository = new MarvelRepositoryImpl(this);

        init();

        favoriteCharactersPresenter = new FavoriteCharactersPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this, marvelRepository);
    }

    private void init() {
        favoriteCharacters = new ArrayList<>();

        favCharactersRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        charactersAdapter = new MarvelCharactersAdapter(this, favoriteCharacters,
                marvelRepository, this);

        favCharactersRecycler.setAdapter(charactersAdapter);

        if (favCharactersToolbar != null) {
            favCharactersToolbar.setTitle(R.string.favorite_characters_activity_toolbar_title);
            setSupportActionBar(favCharactersToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.charactersAdapter != null && this.charactersAdapter.getItemCount() == 0) {
            favoriteCharactersPresenter.showFavoriteMarvelCharacters();
            favCharactersProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayCharactersRecycler() {
        favCharactersProgressBar.setVisibility(View.GONE);
        showEmptyViewIfNoFavorites();
    }

    private void showEmptyViewIfNoFavorites() {
        if (favoriteCharacters.isEmpty()) {
            favCharactersRecycler.setVisibility(View.GONE);
            emptyCharactersLinear.setVisibility(View.VISIBLE);
        }
        else {
            favCharactersRecycler.setVisibility(View.VISIBLE);
            emptyCharactersLinear.setVisibility(View.GONE);
        }
    }

    // ADAPTER CALLBACK
    @Override
    public void setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite) {
        favoriteCharactersPresenter.removeCharacterFromFavorites(character, isFavorite);
    }

    @Override
    public void showCharacterDetail(MarvelCharacter character) {
        Intent showCharacterDetailIntent = new Intent(FavoriteMarvelCharactersActivity.this,
                                                      MarvelCharacterDetailActivity.class);
        showCharacterDetailIntent.putExtra(MARVEL_CHARACTER, character);
        startActivity(showCharacterDetailIntent);
    }


    // PRESENTER CALLBACKS
    @Override
    public void onFavoriteCharactersRetrieved(List<MarvelCharacter> characters) {
        favoriteCharacters = characters;
        charactersAdapter.updateMarvelCharactersAdapter(favoriteCharacters);
        displayCharactersRecycler();
    }

    @Override
    public void onCharacterRemovedFromFavorite(long characterId) {
        // Removing the favorite character and updating the favorites list.
        charactersAdapter.removeCharacterById(characterId);
        favoriteCharacters = charactersAdapter.getUpdatedMarvelCharacters();
        showEmptyViewIfNoFavorites();
    }

}
