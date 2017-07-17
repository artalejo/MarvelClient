package example.com.marvel.domain;

import example.com.marvel.R;

/**
 * Utilities.
 */

public class Utils {

    public static int getFavoriteStatusMsgID(int favoriteStatus) {
        int msg;

        switch (favoriteStatus) {
            case -1:
                msg = R.string.marvel_character_favorite_action_error;
                break;
            case 0:
                msg = R.string.marvel_character_removed_from_favorites;
                break;
            case 1:
                msg = R.string.marvel_character_added_to_favorites;
                break;
            default:
                msg = R.string.marvel_character_favorite_action_error;
        }

        return  msg;
    }

}
