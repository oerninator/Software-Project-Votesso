package com.example.mvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Kommentar-Klasse
 */
public class Comment {

    @SerializedName("cid")
    private long id;

    // Text-Inhalt des Kommentars
    private String body;

    // wird nicht genutzt
    private String subject;

    // Informationen zur Sprache
    private String locale;

    // App-Nutzer speziefische
    private long userID;

    // Timestamps vom Backend
    private String createdAtT;
    private String updatedAt;
    private String updatedAtT;

    // wird nicht genutzt
    private int confidenceScore;

    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public Comment(long id, String body, String createdAt, String subject, String locale
            , long userID, String createdAtT, String updatedAt, String updatedAtT
            , int confidenceScore) {
        this.id = id;
        this.body = body;
        this.subject = subject;
        this.locale = locale;
        this.userID = userID;
        this.createdAtT = createdAtT;
        this.updatedAt = updatedAt;
        this.updatedAtT = updatedAtT;
        this.confidenceScore = confidenceScore;
    }

    public long getUserID() {
        return userID;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAtT() {
        return createdAtT;
    }
}
