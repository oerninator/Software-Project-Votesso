package de.cau.inf.se.sopro.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "genres")
public class Genre {

    @PrimaryKey
    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
