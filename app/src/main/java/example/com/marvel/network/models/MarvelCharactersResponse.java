package example.com.marvel.network.models;

/**
 * Marvel characters response.
 */

public class MarvelCharactersResponse {

    private int code;
    private String status;
    private MarvelResponseData data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public MarvelResponseData getData() {
        return data;
    }
}
