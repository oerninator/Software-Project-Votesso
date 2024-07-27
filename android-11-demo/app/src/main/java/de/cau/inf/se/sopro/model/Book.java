package de.cau.inf.se.sopro.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "books")
public class Book implements Serializable {

    @PrimaryKey
    private Long id;

    private Author author;

    private List<Genre> genres;

    private String title;

    private String isbn;

    @ColumnInfo(name = "price_in_cents")
    private int priceInCents;

    private boolean needsSyncUpdate = false;

    public Book(String title, Author author, String isbn, int priceInCents, List<Genre> genres) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.priceInCents = priceInCents;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return author + ": " + title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public boolean isNeedsSyncUpdate() {
        return needsSyncUpdate;
    }

    public void setNeedsSyncUpdate(boolean needsSyncUpdate) {
        this.needsSyncUpdate = needsSyncUpdate;
    }
}
