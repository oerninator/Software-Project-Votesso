package com.example.mvvm.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Interface das die Datenbankanfragen für die interne Datenbank definiert.
 */
@Dao
public interface UserDao {

    /**
     * Methode zum Einfügen eines gegebenen Users in die Datenbank.
     *
     * @param user User der eingefügt werden soll
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    /**
     * Methode zum Auslesen des eingespeicherten Users aus der Datenbank.
     * Der User ist immer mit id = 1 eingespeichert.
     *
     * @return User aus Datenbank
     */
    @Query("SELECT * FROM user WHERE id=1 LIMIT 1")
    User getUser();

    /**
     * Zählt die Einträge in der Datenbank und gibt die Anzahl zurück.
     *
     * @return Anzahl der Einträge
     */
    @Query("SELECT COUNT(*) FROM user")
    int getUserCount();
}
