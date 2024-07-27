package de.cau.inf.se.sopro.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BookDao {

    @Query("SELECT * FROM books")
    Single<List<Book>> findAll();

    @Query("SELECT * FROM books WHERE id=:id LIMIT 1")
    Single<Book> findOneById(Long id);

    @Query("SELECT * FROM books WHERE isbn=:isbn LIMIT 1")
    Single<Book> findOneByIsbn(String isbn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertBooks(List<Book> books);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Book book);

    @Delete
    Completable deleteOne(Book book);

    @Delete
    Completable deleteAll(List<Book> books);

}
