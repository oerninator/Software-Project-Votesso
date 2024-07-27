package com.example.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Projekt-Klasse die alle vom Backend übergebenen Attribute aufnehmen kann.
 */
public class Project {

    @SerializedName("pid")
    private long id;

    // Informationen zur Projektphase
    private String phase;

    // Projekt Titel
    private String title;

    // URL für Bilddatei
    @SerializedName("picture")
    private String image;

    // Projekt Informationstext
    private String description;

    // Informationen zur Sprache
    private String locale;

    // Timestamps aus der Backend Datenbank
    private String createdAt;
    private String createdAtB;
    private String updatedAt;
    private String updatedAtB;

    // Liste der Teilprojekte zu diesem Projekt
    @SerializedName("subProjects")
    private List<Subproject> subprojects;

    // *********************************************************************************************
    // Konstruktoren, Getter, Setter

    public Project(long id, String phase, String title, String image, String description
            , String locale, String createdAt, String createdAtB, String updatedAt
            , String updatedAtB, List<Subproject> subprojects) {
        this.id = id;
        this.phase = phase;
        this.title = title;
        this.image = image;
        this.description = description;
        this.locale = locale;
        this.createdAt = createdAt;
        this.createdAtB = createdAtB;
        this.updatedAt = updatedAt;
        this.updatedAtB = updatedAtB;
        this.subprojects = subprojects;
    }

    public long getId() {
        return id;
    }

    public String getPhase() {
        return phase;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public List<Subproject> getSubprojects() {
        return subprojects;
    }

    public void setSubprojects(List<Subproject> subprojects) {
        this.subprojects = subprojects;
    }
}
