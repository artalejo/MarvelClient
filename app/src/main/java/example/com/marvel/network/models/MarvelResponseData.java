package example.com.marvel.network.models;

import java.util.List;

/**
 * Marvel response data.
 */

public class MarvelResponseData {

    private int limit;
    private int total;
    private int count;
    private List<MarvelCharacter> results;

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public List<MarvelCharacter> getResults() {
        return results;
    }
}
