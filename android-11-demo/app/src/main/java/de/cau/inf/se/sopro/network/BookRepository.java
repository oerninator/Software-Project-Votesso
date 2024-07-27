package de.cau.inf.se.sopro.network;

import java.util.List;

import javax.inject.Inject;

import de.cau.inf.se.sopro.model.Book;
import de.cau.inf.se.sopro.persistence.BookDatabase;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public class BookRepository {

    private final BookService isbnService;
    private final BookDatabase bookDatabase;

    @Inject
    public BookRepository(BookService isbnService, BookDatabase bookDatabase) {
        this.isbnService = isbnService;
        this.bookDatabase = bookDatabase;
    }

    // Network
    public Call<Book> updateBook(Book b) {
        return isbnService.updateBook(b.getId(), b.getAuthor().getId(), b.getTitle(),
                b.getIsbn(), b.getPriceInCents());
    }

    public Call<List<Book>> getBookByIsbn(String isbn) {
        return isbnService.getBookByIsbn(isbn);
    }

    public Call<List<Book>> getAllBooks() {
        return isbnService.getAllBooks();
    }

    // Database
    public Completable insertBooksIntoDatabase(List<Book> books) {
        return this.bookDatabase.getBookDao().insertBooks(books);
    }

    public Single<List<Book>> getAllBooksFromDatabase() {
        return this.bookDatabase.getBookDao().findAll();
    }
}