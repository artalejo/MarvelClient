package example.com.marvel.network.services;

import example.com.marvel.network.MarvelClient;
import example.com.marvel.network.models.MarvelCharactersResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class MarvelServices {

    public static MarvelClient.MarvelServicesInterface marvelClient;
    public static int CHARACTERS_REQUEST_LIMIT = 50;

    public MarvelServices(MarvelClient.MarvelServicesInterface servicesInterface) {
        this.marvelClient = servicesInterface;
    }

    public void getCharactersService(long charactersOffset, Callback<MarvelCharactersResponse> getCharactersCallback) {

        Call<MarvelCharactersResponse> getCharactersCall =
                this.marvelClient.getCharacters(CHARACTERS_REQUEST_LIMIT, charactersOffset);
        getCharactersCall.enqueue(getCharactersCallback);
    }
}
