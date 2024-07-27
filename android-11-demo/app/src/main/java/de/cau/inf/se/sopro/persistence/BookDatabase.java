package de.cau.inf.se.sopro.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.cau.inf.se.sopro.model.Author;
import de.cau.inf.se.sopro.model.Book;
import de.cau.inf.se.sopro.model.BookDao;
import de.cau.inf.se.sopro.model.Genre;

@Database(version = 1, entities = {Book.class, Author.class, Genre.class})
@TypeConverters({Converter.class})
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao getBookDao();
}
