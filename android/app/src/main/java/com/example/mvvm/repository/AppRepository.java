package com.example.mvvm.repository;

import android.util.Log;

import com.example.mvvm.model.Comment;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.model.Vote;
import com.example.mvvm.network.APIService;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.AppViewModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasse die für das Bereitstellen der Projektdaten zurständig ist.
 */
public class AppRepository {

    // Variable für den API Service der über Retrofit bereitgestellt werden kann
    private APIService apiService;

    // Klassenvariable die die Instanz des Repositories hält.
    private static AppRepository instance;

    //Singleton pattern
    public static synchronized AppRepository getRepositoryInstance() {
        if (instance == null) {
            instance = new AppRepository();
        }
        return instance;
    }

    /**
     * Methode die einen API Call macht, um die gesamten Daten über bestehende Projekte
     * vom Backend abzufragen.
     */
    public void makeApiCall() {
        apiService = RetrofitInstance.getRetrofitClient().create(APIService.class);
        Call<List<Project>> call = apiService.getProjectList();
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.isSuccessful() && dataIsValid(response.body())) {
                    AppViewModel._getProjectList().postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                AppViewModel._getProjectList().postValue(null);
            }
        });

    }

    /**
     * Überprüft die gegebenen Projektdaten
     *
     * @param body Projektdaten
     * @return True wenn valide
     */
    private boolean dataIsValid(List<Project> body) {
        // Aktuell nur NotNull-Checks
        for (Project project : body) {
            if(project.getSubprojects() == null){
                return false;
            }
            for (Subproject subproject : project.getSubprojects()){
                if (subproject.getVotesList() == null){
                    return false;
                }
                if (subproject.getCommentsList() == null){
                    return  false;
                }
            }
        }
        return true;
    }

    /**
     * Macht einen API Call um ein gegebenes Kommentar zu einer gegebenen Teilprojekt-ID
     * an das Backend weiterleitet, um es in der Datenbank abzuspeichern.
     *
     * @param sid Teilprojekt-ID
     * @param body Kommentar welches verschickt werden soll
     */
    public void makeApiCallForComment(long sid, Comment body) {
        //apiService != null, da dieser bei Hochladen eines Kommentars auf jeden Fall initialisert wurde
        assert (apiService != null);
        Call<ResponseBody> call = apiService.uploadComment(sid, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("UploadCommentCode: ", "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("UploadCommentFailure: ", t.toString());
            }
        });
    }

    /**
     * Macht einen API Call um ein gegebenen Vote mit gegebener Teilprojekt-ID
     * an das Backend weiterzuleiten und zu speichern.
     *
     * @param sid Teilprojekt-ID
     * @param body Vote welcher verschickt werden soll
     */
    public void makeApiCallForVote(long sid, Vote body) {
        assert (apiService != null);
        Call<ResponseBody> call = apiService.uploadVote(sid, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("UploadVote: ", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("UploadVote: ", t.toString());
            }
        });
    }
}
