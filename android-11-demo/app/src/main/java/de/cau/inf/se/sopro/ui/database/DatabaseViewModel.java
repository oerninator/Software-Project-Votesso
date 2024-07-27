package de.cau.inf.se.sopro.ui.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import de.cau.inf.se.sopro.model.Book;
import de.cau.inf.se.sopro.network.BookRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class DatabaseViewModel extends ViewModel {

    private final MutableLiveData<List<Book>> _books = new MutableLiveData<>();
    private final BookRepository bookRepository;

    @Inject
    public DatabaseViewModel(SavedStateHandle savedStateHandle, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        _books.setValue(new ArrayList<>());
    }

    public void synchronizeAllBooks() {

        List<Book> updatableBooks = _books.getValue().stream()
                .filter(book -> book.isNeedsSyncUpdate()).collect(Collectors.toList());

        for(int i = 0; i < updatableBooks.size(); i++) {

            Book b = updatableBooks.get(i);

            int finalI = i;
            bookRepository.updateBook(b).enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.isSuccessful()) {
                        Log.d("SoPro", "Updated backend.");
                    } else {
                        Log.e("SoPro", "Could not update book. Response: " + response);
                    }
                    b.setNeedsSyncUpdate(false);
                    if(finalI + 1 == updatableBooks.size()) {
                        // last book, call update all books upon callback
                        requestAllBooksFromBackend();
                    }
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    Log.e("SoPro", "Could not update book.", t);
                    b.setNeedsSyncUpdate(false);
                    if(finalI + 1 == updatableBooks.size()) {
                        // last book, call update all books upon callback
                        requestAllBooksFromBackend();
                    }
                }
            });
        }

        if(updatableBooks.isEmpty()) {
            requestAllBooksFromBackend();
        }
    }

    private void requestAllBooksFromBackend() {
        bookRepository.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> requestedBooks = response.body();
                    _books.setValue(requestedBooks);

                    // update Room DB
                    bookRepository.insertBooksIntoDatabase(requestedBooks).subscribeOn(Schedulers.io()).subscribe(() -> {
                        Log.d("SoPro", "Updated database.");
                    }, throwable -> {
                        Log.e("SoPro", "Could not update database.", throwable);
                    });
                } else {
                    Log.e("SoPro", "Could not sync books. Response: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("SoPro", "Could not reach backend", t);
            }
        });
    }

    public LiveData<List<Book>> getAllBooks() {
        return _books;
    }

    public void loadAllBooksFromDb() {
        bookRepository.getAllBooksFromDatabase().observeOn(AndroidSchedulers.mainThread()).doAfterSuccess(books -> {
            _books.setValue(books);
        }).subscribeOn(Schedulers.io()).subscribe((books) -> {
            Log.d("SoPro", "Retrieved books from database.");
        }, throwable -> {
            Log.e("SoPro", "Could not retrieve books from database.", throwable);
        });
    }
}