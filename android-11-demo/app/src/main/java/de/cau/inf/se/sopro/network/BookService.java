package de.cau.inf.se.sopro.network;

import java.util.List;

import de.cau.inf.se.sopro.model.Book;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService {

    @GET("/api/search/{isbn}")
    Call<List<Book>> getBookByIsbn(@Path("isbn") String isbn);

    @GET("/api/books")
    Call<List<Book>> getAllBooks();

    @PUT("/api/book/{id}")
    Call<Book> updateBook(@Path("id") long id, @Query("authorId") long authorId,
                          @Query("title") String title, @Query("isbn") String isbn,
                          @Query("priceInCents") int priceInCents);

}

