package example.com.marvel.storage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.List;

import example.com.marvel.AndroidApplication;
import example.com.marvel.domain.repository.MarvelRepository;
import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.network.models.MarvelCharactersResponse;
import example.com.marvel.network.services.MarvelServices;
import example.com.marvel.storage.database.DbHelper;
import retrofit2.Callback;

/**
 * Repository implementation that abstracts the way of retrieving the marvel character information.
 */
public class MarvelRepositoryImpl implements MarvelRepository {

    private final DbHelper db;
    private final MarvelServices marvelServices;
    private final AppCompatActivity activity;

    public MarvelRepositoryImpl(Context context) {
        db = DbHelper.getInstance(context);
        activity = (AppCompatActivity) context;
        marvelServices = ((AndroidApplication) activity.getApplication()).getMarvelServices();
    }

    @Override
    public void getMarvelCharacters(long charactersOffset,
                                    Callback<MarvelCharactersResponse> getCharactersCallback) {
        marvelServices.getCharactersService(charactersOffset, getCharactersCallback);
    }

    @Override
    public int setCharacterFavoriteStatus(MarvelCharacter character, boolean isFavorite) {
        if (isFavorite)
            return db.insertFavoriteCharacter(character);
        else
            return db.deleteFavoriteCharacter(character);
    }

    @Override
    public List<MarvelCharacter> getFavoriteMarvelCharacters() {
        return db.getFavoriteCharacters();
    }

    @Override
    public HashSet<Long> getFavoriteMarvelCharactersIds() {
        return db.getFavoriteCharacterIds();
    }
}
