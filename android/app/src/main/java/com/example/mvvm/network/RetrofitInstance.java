package com.example.mvvm.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// https://run.mocky.io/v3/
// http://134.245.1.240:1100/

/**
 * Klasse die eine Retrofit-Instanz bereitstellt um mit dem Backend kommunizieren zu können.
 */
public class RetrofitInstance {

    // URL mit der die Backendabfrage gemacht wird.
    private static String baseURL = getDefaultBaseURL();

    // Standart URL für die Uni VM
    private final static String defaultBaseURL = "http://134.245.1.240:1100/";

    // Halter für die Retrofit-Instanz
    private static Retrofit retrofit;

    /**
     * Erstellt eine Retrofit-Instanz wenn keine vorhanden ist.
     *
     * @return
     */
    public synchronized static Retrofit getRetrofitClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }

    /**
     * Getter für die genutzte URL.
     *
     * @return
     */
    public static String getBaseURL() {
        return baseURL;
    }

    /**
     * Ersetzt die genutzte URL mit der übergebene URL.
     *
     * @param newBaseURl URL die genutzt werden soll
     */
    public static void changeBaseUrl(String newBaseURl) {
        baseURL = newBaseURl;
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL).build();

    }

    /**
     * Getter für die Standart-URL.
     *
     * @return
     */
    public static String getDefaultBaseURL() {
        return defaultBaseURL;
    }
}
