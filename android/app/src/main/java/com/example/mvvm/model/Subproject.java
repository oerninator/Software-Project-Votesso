package com.example.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Teilprojekt-Klasse die alle vom Backend übergebenen Attribute aufnehmen kann.
 */
public class Subproject {

    @SerializedName("sid")
    private long id;

    // Teilprojekt Budget
    private long price;

    // Anzahl der Kommentare
    private int commentsCount;

    // Anzahl der Daumen-Hoch - wird nicht genutzt
    private int votesUp;

    // Anzahl der gesamten Votes - wird nicht genutzt
    @SerializedName("votesCount")
    private int votesDown;

    // Timestamp wann das Projekt erstellt wurde
    private String createdAt;

    // Teilprojekt Informationstext
    private String description;

    // Nicht mehr genutzt
    private double latitude;
    private double longitude;

    // Teilprojekt Titel
    private String title;

    // URL für eine Bilddatei
    @SerializedName("picture")
    private String image;

    // Indikator für die Sprache - wird noch nicht genutzt
    private String locale;

    // Wird nicht genutzt
    private int confidence_score;

    // Klasse die die Geodaten des Teilprojekts enthält
    private MapLocation mapLocation;

    // Liste aller Kommentare zu diesem Teilprojekt
    @SerializedName("comments")
    private List<Comment> commentsList;

    // Liste aller Votes zu diesem Teilprojekt
    @SerializedName("votes")
    private List<Vote> votesList;


    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public Subproject(long id, long price, int commentsCount, int votesUp,
                      int votesDown, String createdAt, String description, double latitude,
                      double longitude, String title, String image,
                      List<Comment> commentsList, List<Vote> votesList, MapLocation mapLocation) {
        this.id = id;
        this.price = price;
        this.commentsCount = commentsCount;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.createdAt = createdAt;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.image = image;
        this.commentsList = commentsList;
        this.votesList = votesList;
        this.mapLocation = mapLocation;
    }

    public MapLocation getMapLocation() {
        return mapLocation;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setVotesUp(int votesUp) {
        this.votesUp = votesUp;
    }

    public void setVotesDown(int votesDown) {
        this.votesDown = votesDown;
    }

    public long getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getVotesUp() {
        return votesUp;
    }

    public int getVotesDown() {
        return votesDown;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public List<Vote> getVotesList() {
        return votesList;
    }

    public void setCommentsList(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    public void setVotesList(List<Vote> votesList) {
        this.votesList = votesList;
    }

    public void setImage(String s) {
    }
}
