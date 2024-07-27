package com.example.mvvm.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mvvm.model.User;
import com.example.mvvm.model.UserDao;

/**
 * Abstrakte Datenbank-Klasse die von Room implementiert wird.
 */
@Database(version = 1, entities = {User.class})
@TypeConverters({Converter.class})
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
