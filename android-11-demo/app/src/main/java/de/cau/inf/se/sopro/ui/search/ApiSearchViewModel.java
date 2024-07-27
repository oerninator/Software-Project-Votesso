package de.cau.inf.se.sopro.ui.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import de.cau.inf.se.sopro.model.Book;
import de.cau.inf.se.sopro.network.BookRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ApiSearchViewModel extends ViewModel {

    private final MutableLiveData<List<Book>> _books = new MutableLiveData<>();
    private final BookRepository isbnRepository;

    @Inject
    public ApiSearchViewModel(SavedStateHandle savedStateHandle, BookRepository isbnRepository) {
        this.isbnRepository = isbnRepository;
    }

    public Call lookupBooksByIsbn(String isbn) {
        Call<List<Book>> userCall = isbnRepository.getBookByIsbn(isbn);
        userCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    _books.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("SoPro", "Could not reach backend", t);
            }
        });
        return userCall;
    }

    public LiveData<List<Book>> getAllBooks() {
        return _books;
    }
}