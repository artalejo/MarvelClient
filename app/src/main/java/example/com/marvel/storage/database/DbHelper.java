package example.com.marvel.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import example.com.marvel.network.models.MarvelCharacter;

/**
 * Sqlite Database Helper that handles the offline favorite characters.
 */
public class DbHelper extends SQLiteOpenHelper {

    //Singleton
    private static final String DATABASE_NAME = "marvel_db";
    private static final int DATABASE_VERSION = 1;

    public static final String FAVORITE_CHARACTER_TABLE = "favorite_characters";
    private static final String CHARACTER_ID = "character_id";
    private static final String CHARACTER_JSON = "character_json";

    private static DbHelper dbInstance = null;
    private final Gson gson;
    private Context context;

    public static synchronized DbHelper getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DbHelper(context);
        }
        return dbInstance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.gson = new Gson();
    }

    private static String CREATE_FAVORITE_CHARACTERS_TABLE = "CREATE TABLE favorite_characters (" +
            " character_id INT PRIMARY KEY," +
            " character_json TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_CHARACTERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // DB FUNCTIONS

    // -1: Error
    // 0: Favorite Removed
    // 1: Favorite Added

    public int insertFavoriteCharacter(MarvelCharacter character) {
        if (character == null)
            return -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues characterValue = new ContentValues();
        characterValue.put(CHARACTER_ID, character.getId());
        characterValue.put(CHARACTER_JSON, gson.toJson(character));
        db.insertWithOnConflict(FAVORITE_CHARACTER_TABLE, null, characterValue,
                                SQLiteDatabase.CONFLICT_REPLACE);
        return 1;
    }


    public int deleteFavoriteCharacter(MarvelCharacter character) {
        if (character == null)
            return -1;

        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(FAVORITE_CHARACTER_TABLE, CHARACTER_ID + " = ?",
                new String[]{String.valueOf(character.getId())});

        return delete == 1 ? 0 : -1;
    }


    public ArrayList<MarvelCharacter> getFavoriteCharacters(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<MarvelCharacter> favoriteCharacters = new ArrayList<>();
        String getCharactersQuery = "SELECT character_json FROM favorite_characters";
        MarvelCharacter character;

        Cursor cursor = db.rawQuery(getCharactersQuery, null);

        try {
            while (cursor.moveToNext()) {
                String characterJson = cursor.getString(0);
                character = gson.fromJson(characterJson, MarvelCharacter.class);
                favoriteCharacters.add(character);
            }
        } finally {
            cursor.close();
        }
        return favoriteCharacters;
    }

    public HashSet<Long> getFavoriteCharacterIds(){

        SQLiteDatabase db = this.getWritableDatabase();
        HashSet<Long> favoriteIds = new HashSet<>();
        String getCharactersQuery = "SELECT character_id FROM favorite_characters";

        Cursor cursor = db.rawQuery(getCharactersQuery, null);

        try {
            while (cursor.moveToNext()) {
                long characterID = cursor.getLong(0);
                favoriteIds.add(characterID);
            }
        } finally {
            cursor.close();
        }
        return favoriteIds;
    }
}
