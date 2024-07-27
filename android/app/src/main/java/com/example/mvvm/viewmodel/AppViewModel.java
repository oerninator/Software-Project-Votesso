package com.example.mvvm.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import com.example.mvvm.activities.SubprojectActivity;
import com.example.mvvm.model.Comment;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.model.User;
import com.example.mvvm.model.Vote;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.persistence.UserDatabase;
import com.example.mvvm.repository.AppRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasse die die gesamte/meiste Logik innerhalt der App enthält.
 */
public class AppViewModel extends ViewModel {

    // Name des Users für die bessere Zugreifbarkeit.
    private static String userName = null;

    // ID des Useres für die bessere Zugreifbarkeit.
    private static int userID = -1;

    /**
     * Konstruktor der die Projekt-Liste initialisiert.
     */
    public AppViewModel(){
        projectList = new MutableLiveData<>();
    }

    // *********************************************************************************************
    // LiveData

    // Klassenvariable LiveData die die Projektdaten bereitstellt.
    private static MutableLiveData<List<Project>> projectList;

    /**
     * Nur im Repository zu nutzen für die Datenbereitstellung.
     * Müsste eigentlich anders ins ViewModel gelagen.
     */
    public static MutableLiveData<List<Project>> _getProjectList() {
        return projectList;
    }

    /**
     * Methode um die Projektdaten außerhalb des ViewModels einsehen zu können.
     * @return
     */
    public static LiveData<List<Project>> getProjectList() {
        return (LiveData<List<Project>>) projectList;
    }

    // *********************************************************************************************
    // Generell genutzte Methoden


    /**
     * Methode zum holen aktueller Projektdaten vom backend.
     */
    public void getData(){
        AppRepository.getRepositoryInstance().makeApiCall();
    }

    /**
     * Methode zum versenden eines gegeben Kommentars.
     *
     * @param sid Teilprojekt-ID zu welchem das Kommentar gehört
     *
     */
    public void sentComment(Context context,long sid, String str){
        Comment comment = new Comment(0 ,str,null , null, "de",userGetId(context),null,null,null,0);

        AppRepository.getRepositoryInstance().makeApiCallForComment(sid, comment);
    }

    /**
     * Methode, die eine Kopie der aktuellen Projektdaten zurückgibt.
     *
     * @return
     */
    public static List<Project> getProjects() {
        return projectList.getValue();
    }

    /**
     * Getter für den User-Namen
     *
     * @return
     */
    public static String getUserName(){
        return userName;
    }

    /**
     * Getter für die User-ID.
     *
     * @return
     */
    public static int getUserID(){
        return userID;
    }

    /**
     * Setzt die Klassenvariable User-Name mit dem gegeben Namen.
     *
     * @param name User-Name
     */
    public static void setUserName(String name){
        userName =name;
    }

    /**
     * Setzt die Klassenvariable User-ID mit der gegeben ID.
     *
     * @param id User-ID
     */
    public static void setUserID(int id){
        userID = id;
    }

    /**
     * Methode die eine Teilprojekt-Liste über eine gegebene ID in den aktuellen Projektdaten
     * sucht und zurückgibt.
     *
     * @param pid Projekt-ID
     * @return Teilprojekt-Liste mit gegebener ID
     */
    public static List<Subproject> getSubprojectListByPID(long pid) {
        for(Project project : projectList.getValue()) {
            if(project.getId() == pid) {
                return project.getSubprojects();
            }
        }
        return null;
    }

    /**
     * Methode die ein Projekt über eine gegebene ID in den aktuellen Projektdaten
     * sucht und zurückgibt.
     *
     * @param pid Projekt-ID
     * @return Projekt mit gegebener ID
     */
    public static  Project getProjectByPID(long pid) {
        for(Project project : projectList.getValue()) {
            if(project.getId() == pid) {
                return project;
            }
        }
        Log.d("getProjectByPID", "Fehler aufgetreten");
        return null;
    }

    /**
     * Methode die ein Teilprojekt über eine gegebene ID in den aktuellen Projektdaten
     * sucht und zurückgibt.
     *
     * @param sid Teilprojekt-ID
     * @return Teilprojekt mit gegebener ID
     */
    public static Subproject getSubprojectBySID(long sid) {
        for(Project project : projectList.getValue()) {
            for(Subproject subproject : project.getSubprojects()) {
                if(subproject.getId() == sid) {
                    return subproject;
                }
            }
        }
        return null;
    }

    /**
     * Methode zum ändern der Basis-URL.
     *
     * @param url Neue Basis-URL
     */
    public static void changeBaseURLAVM(String url) {
        RetrofitInstance.changeBaseUrl(url);
    }

    /**
     * Methode die eine gegebene Kommentar-Liste nach ihren Zeitstempeln sortiert.
     *
     * @param comments Kommentar-Liste
     */
    public static void sortCommentsBasedOnTimestamp(ArrayList<Comment> comments) {
        Collections.sort(comments, new Comparator<Comment>() {
            public int compare(Comment o1, Comment o2) {
                return -o1.getCreatedAtT().compareTo(o2.getCreatedAtT());
            }  		});
    }

    // *********************************************************************************************
    // Vote Methoden

    /**
     * Methode die alle Funktionen für das Klicken des 'Daumen-Hoch' bereitstellt.
     *
     * @param context
     */
    public void clickThumbUp(Context context){
        Vote newVote = new Vote(0,true);

        List<Project> _projectList = projectList.getValue();

        Subproject _subproject = getSubprojectBySID(SubprojectActivity.getSid());

        long id = getMaxVoteId() + 1;
        newVote = new Vote(id, true);
        _subproject.getVotesList().add(newVote);
        AppRepository.getRepositoryInstance().makeApiCallForVote(SubprojectActivity.getSid(), newVote);

        userVoteSubproject(context,SubprojectActivity.getSid());

        projectList.postValue(_projectList);
    }

    /**
     * Methode die alle Funktionen für das Klicken des 'Daumen-Runter' bereitstellt.
     *
     * @param context
     */
    public void clickThumbDown(Context context){
        Vote newVote = new Vote(0,true);

        List<Project> _projectList = projectList.getValue();

        Subproject _subproject = getSubprojectBySID(SubprojectActivity.getSid());

        long id = getMaxVoteId() + 1;
        newVote = new Vote(id, false);
        _subproject.getVotesList().add(newVote);
        AppRepository.getRepositoryInstance().makeApiCallForVote(SubprojectActivity.getSid(), newVote);

        userVoteSubproject(context,SubprojectActivity.getSid());

        projectList.postValue(_projectList);
    }

    /**
     * Zählt alle positiven Bewertungen für ein gegebenes Teilprojekt.
     *
     * @param sid Teilprojekt-ID
     * @return Anzahl der positiven Bewertungen
     */
    public static int getVoteUpCount(long sid){
        int voteUpCount = 0;
        Subproject subproject = getSubprojectBySID(sid);
        for (Vote vote : subproject.getVotesList()){
            if(vote.isVoteFlag()){
                voteUpCount++;
            }
        }
        return voteUpCount;
    }

    /**
     * Zählt alle negativen Bewertungen für ein gegebenes Teilprojekt.
     *
     * @param sid Teilprojekt-ID
     * @return Anzahl der negativen Bewertungen
     */
    public static int getVoteDownCount(long sid){
        int voteDownCount = 0;
        Subproject subproject = getSubprojectBySID(sid);
        for (Vote vote : subproject.getVotesList()){
            if(!vote.isVoteFlag()){
                voteDownCount++;
            }
        }
        return voteDownCount;
    }

    // *********************************************************************************************
    // Datenbank Methoden

    /**
     * Gibt den in der Datenbank gespeicherten Nutzer zurück.
     *
     * @param context
     * @return Nutzer
     */
    public User getUser(Context context){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        int userCount = db.getUserDao().getUserCount();
        if(userCount >= 1){
            return db.getUserDao().getUser();
        } else{
            return null;
        }
    }

    /**
     * Erstellt einen neuen Nutzereintrag in der Datenbank mit dem gegebenen Namen und
     * einer zufälligen ID. Ein vorhandener Eintrag wird überschrieben.
     *
     * @param context
     * @param name Name des Nutzers
     */
    public void insertUser(Context context, String name){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        List<Long> vList = new ArrayList<>();
        vList.add((long) ThreadLocalRandom.current().nextInt(100000000, 999999999));
        User user = new User(1, name, vList, new ArrayList<>());
        db.getUserDao().insertUser(user);
    }

    /**
     * Gibt die ID des App-Nutzers zurück.
     *
     * @param context
     * @return Nutzer-ID
     */
    public int userGetId(Context context){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        User user = db.getUserDao().getUser();
        Long l = user.getVoteIDs().get(0);
        if(l != null){
            return (int) l.longValue();
        }else {
            return -1;
        }
    }

    /**
     * Checkt die Datenbankliste ob eine gegebene Teilprojekt-ID enthalten ist.
     *
     * @param context
     * @param id Gegebene Teilprojekt-ID
     * @return True, wenn Sie enthalten ist. False, wenn nicht.
     */
    public boolean userCheckSubprojectVoted(Context context, long id){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        User user = db.getUserDao().getUser();
        for(Long l : user.getCommentIDs()){
            if(l.equals(id)){
                db.close();
                return true;
            }
        }
        db.close();
        return false;
    }

    /**
     * Speichert die gegebene Teilprojekt-ID in einer Liste in der Datenbank.
     * @param context
     * @param id Teilprojekt-ID für die gevotet wurde.
     */
    public void userVoteSubproject(Context context, long id){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        User user = db.getUserDao().getUser();
        user.getCommentIDs().add(id);
        db.getUserDao().insertUser(user);
    }

    /**
     * Löscht alle Einträge der Datenbank. Nutzen ist vor allem beim Testen.
     * @param context
     */
    public static void clearDB(Context context){
        UserDatabase db = Room
                .databaseBuilder(context, UserDatabase.class, "user_database")
                .allowMainThreadQueries()
                .build();
        db.clearAllTables();
    }

    /**
     * Diese Methode geht alle Votes in allen Subprojekten aller Projekte der Projekt-Liste
     * durch und gibt die höchste gefundene Vote-ID als 'long' zurück.
     *
     * ---> Die Vergabe der IDs findet aktuell aber über das Backend statt, daher nicht notwendig.
     *
     * @return Höchste Vote-ID in der aktuellen Projekt-Liste
     */
    public long getMaxVoteId(){
        List<Project> _projectList = projectList.getValue();
        long maxId = 0;
        for (Project project : _projectList){
            for (Subproject subproject : project.getSubprojects()){
                for (Vote vote : subproject.getVotesList()){
                    if(vote.getId() > maxId){
                        maxId = vote.getId();
                    }
                }
            }
        }
        return maxId;
    }
}
