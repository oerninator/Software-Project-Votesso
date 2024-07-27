package com.example.mvvm.network;

import com.example.mvvm.model.Comment;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.model.Vote;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface das alle Methoden, die Retrofit zum Kommunizieren bereitstellen soll, aufzählt.
 */
public interface APIService {

    /**
     * POST-Request-Methode die zum Hochladen eines Kommentars genutzt wird.
     *
     * @param sid Teilprojekt-ID zu welchem das Kommentar zugeordnet werden soll
     * @param body Kommentar das hochgeladen werden soll
     * @return
     */
    @POST("/api/comment/{sid}")
    Call<ResponseBody> uploadComment(@Path("sid") long sid, @Body Comment body);

    /**
     * POST-Request-Methode die zum Hochladen eines Votes genutzt wird.
     *
     * @param sid Teilprojekt-ID zu welchem der Vote zugeordnet werden soll
     * @param body Vote der hochgeladen werden soll
     * @return
     */
    @POST("/api/vote/{sid}")
    Call<ResponseBody> uploadVote(@Path("sid") long sid, @Body Vote body);

    /**
     * GET-Request-Methode die genutzt werden kann um alle Projektdaten abzufragen.
     *
     * @return Liste der Projekte
     */
    @GET("/api/projects")
    Call<List<Project>> getProjectList();


}
