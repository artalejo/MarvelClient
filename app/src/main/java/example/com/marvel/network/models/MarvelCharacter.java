package example.com.marvel.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Marvel Character model
 */

public class MarvelCharacter implements Parcelable {

    public static String MARVEL_CHARACTER_TYPE = "marvel_character";
    public static String LOADING_TYPE = "loading";
    private long id;
    private String name;
    private String description;
    @SerializedName("thumbnail") private CharacterThumbnail characterThumbnail;
    private String type; // Used for the endlessScroll
    private boolean favorited;


    public MarvelCharacter() {
        this.type = MARVEL_CHARACTER_TYPE;
    }

    public MarvelCharacter(String type) {
        this.type = type;
    }

    public MarvelCharacter(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCharacterThumbnail() {
        return characterThumbnail.getThumbnailUrl();
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    // PARCELABLE IMPLEMENTATION

    protected MarvelCharacter(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        characterThumbnail = in.readParcelable(CharacterThumbnail.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(characterThumbnail, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MarvelCharacter> CREATOR = new Parcelable.Creator<MarvelCharacter>() {
        @Override
        public MarvelCharacter createFromParcel(Parcel in) {
            return new MarvelCharacter(in);
        }

        @Override
        public MarvelCharacter[] newArray(int size) {
            return new MarvelCharacter[size];
        }
    };

}
