package example.com.marvel;

import android.app.Application;

import com.facebook.stetho.Stetho;

import example.com.marvel.network.MarvelClient;
import example.com.marvel.network.services.MarvelServices;

/**
 * Android app.
 */
public class AndroidApplication extends Application {


    private MarvelClient.MarvelServicesInterface marvelApiClient;
    private MarvelServices marvelServices;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        marvelApiClient = MarvelClient.getApiClient();
        marvelServices = new MarvelServices(marvelApiClient);
    }

    public MarvelServices getMarvelServices() {
        return marvelServices;
    }
}
