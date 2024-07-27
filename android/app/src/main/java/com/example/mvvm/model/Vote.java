package com.example.mvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model-Klasse für den Vote zu einem Teilprojekt.
 */
public class Vote {

    // Datenbank-ID
    @SerializedName("vid")
    private long id;

    // True für Daumen-Hoch, False für Daumen-Runter
    @SerializedName("flag")
    private boolean voteFlag;


    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public Vote(long id, boolean voteFlag) {
        this.id = id;
        this.voteFlag = voteFlag;
    }

    public long getId() {
        return id;
    }
    public boolean isVoteFlag() {
        return voteFlag;
    }
}
