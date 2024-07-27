package com.example.mvvm.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Entity-User-Klasse für die User-Datenbank.
 */
@Entity(tableName = "user")
public class User implements Serializable {

    @PrimaryKey
    private long id;

    // User name, eindeutig beim ersten Start der App auszuwählen
    private String name;

    // Liste von IDs - wird aktuell nur zum Speichern der User-ID genutzt.
    private List<Long> voteIDs;

    // Liste von IDs - wird genutzt um Teilprojekt-IDs zu speichern bei denen schon gevotet wurde.
    private List<Long> commentIDs;

    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public User(long id, String name, List<Long> voteIDs, List<Long> commentIDs) {
        this.id = id;
        this.name = name;
        this.voteIDs = voteIDs;
        this.commentIDs = commentIDs;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getVoteIDs() {
        return voteIDs;
    }

    public List<Long> getCommentIDs() {
        return commentIDs;
    }
}
