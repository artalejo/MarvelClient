package example.com.marvel.domain.storage;


import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.HashSet;

import example.com.marvel.network.models.MarvelCharacter;
import example.com.marvel.storage.database.DbHelper;

import static example.com.marvel.storage.database.DbHelper.FAVORITE_CHARACTER_TABLE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DbHelperTest {

    private DbHelper dbHelper;
    private MarvelCharacter sampleCharacter;
    final long characterID = 1;
    final String characterName = "testName";
    final String characterDescription = "testDescription";


    @Before
    public void setUp() throws Exception {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        dbHelper = DbHelper.getInstance(context.getApplicationContext());
        sampleCharacter = new MarvelCharacter(characterID, characterName, characterDescription);
    }

    @After
    public void tearDown() throws Exception {
        cleanUpDatabase(new String[]{FAVORITE_CHARACTER_TABLE,});
    }

    private void cleanUpDatabase(String[] tables) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (String table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        dbHelper.onCreate(db);
        dbHelper.close();
    }

    @Test
    public void checkDatabaseName() {
        final String EXPECTED_DATABASE_NAME = "marvel_db";
        assertEquals(EXPECTED_DATABASE_NAME, dbHelper.getDatabaseName());
    }

    @Test
    public void insertFavoriteCharacterReturnsFalseWhenNullCharacter() {
        int result = dbHelper.insertFavoriteCharacter(null);
        assertEquals(result, -1);
    }

    @Test
    public void insertFavoriteMarvelCharacterReturnsTrueWhenCharacterAdded() {
        int result = dbHelper.insertFavoriteCharacter(sampleCharacter);
        HashSet<Long> favoriteCharacterIds = dbHelper.getFavoriteCharacterIds();
        ArrayList<MarvelCharacter> favoriteCharacters = dbHelper.getFavoriteCharacters();
        assertEquals(result, 1);
        assertEquals(favoriteCharacterIds.size(), 1);
        assertEquals(favoriteCharacters.size(), 1);
    }

    @Test
    public void deleteFavoriteMarvelCharacterReturnsFalseWhenNullCharacter() {
        int result = dbHelper.deleteFavoriteCharacter(null);
        assertEquals(result, -1);
    }

    @Test
    public void deleteFavoriteMarvelCharacterReturnsTrueWhenCharacterRemoved() {
        // Adding marvel character and later on removing it.
        dbHelper.insertFavoriteCharacter(sampleCharacter);
        HashSet<Long> favoriteCharacterIds = dbHelper.getFavoriteCharacterIds();
        ArrayList<MarvelCharacter> favoriteCharacters = dbHelper.getFavoriteCharacters();
        assertEquals(favoriteCharacterIds.size(), 1);
        assertEquals(favoriteCharacters.size(), 1);
        // Removing the favorite just added
        int removedResult = dbHelper.deleteFavoriteCharacter(sampleCharacter);
        favoriteCharacterIds = dbHelper.getFavoriteCharacterIds();
        favoriteCharacters = dbHelper.getFavoriteCharacters();
        assertEquals(removedResult, 0);
        assertEquals(favoriteCharacterIds.size(), 0);
        assertEquals(favoriteCharacters.size(), 0);
    }

    @Test
    public void deleteFavoriteMarvelCharacterReturnsFalseWhenCharacterToRemoveDoesNotExist() {
        int result = dbHelper.deleteFavoriteCharacter(sampleCharacter);
        assertEquals(result, -1);
    }

    @Test
    public void getFavoriteCharactersReturnCharacterJustAdded() {
        int addedCharacterResult = dbHelper.insertFavoriteCharacter(sampleCharacter);
        ArrayList<MarvelCharacter> favoriteCharacters = dbHelper.getFavoriteCharacters();

        assertEquals(addedCharacterResult, 1);
        assertEquals(characterID, favoriteCharacters.get(0).getId());
        assertEquals(characterName, favoriteCharacters.get(0).getName());
        assertEquals(characterDescription, favoriteCharacters.get(0).getDescription());
    }

    @Test
    public void getFavoriteCharactersReturnsEmptyListWhenNoFavoriteCharacters() {
        ArrayList<MarvelCharacter> favoriteCharacters = dbHelper.getFavoriteCharacters();
        assertTrue(favoriteCharacters.size() == 0);
    }

    @Test
    public void getFavoriteCharacterIdsReturnsListWithFavoriteCharacterJustAdded() {
        HashSet<Long> favoriteCharactersIds = dbHelper.getFavoriteCharacterIds();
        assertTrue(favoriteCharactersIds.size() == 0);
    }

    @Test
    public void getFavoriteCharacterIdsReturnsEmptyListWhenNoFavoriteCharacters() {
        int result = dbHelper.insertFavoriteCharacter(sampleCharacter);
        HashSet<Long> favoriteCharactersIds = dbHelper.getFavoriteCharacterIds();
        assertEquals(result, 1);
        assertTrue(favoriteCharactersIds.size() == 1);
        assertTrue(favoriteCharactersIds.contains(characterID));
    }

}
