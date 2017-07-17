package example.com.marvel.network.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Character Thumbnail model.
 */

public class CharacterThumbnail implements Parcelable {

    private String path;
    private String extension;


    public String getThumbnailUrl(){
        return path + "." + extension;
    }

    // PARCELABLE IMPLEMENTATION

    protected CharacterThumbnail(Parcel in) {
        path = in.readString();
        extension = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(extension);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CharacterThumbnail> CREATOR = new Parcelable.Creator<CharacterThumbnail>() {
        @Override
        public CharacterThumbnail createFromParcel(Parcel in) {
            return new CharacterThumbnail(in);
        }

        @Override
        public CharacterThumbnail[] newArray(int size) {
            return new CharacterThumbnail[size];
        }
    };

}
