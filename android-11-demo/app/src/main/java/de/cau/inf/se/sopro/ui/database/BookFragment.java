package de.cau.inf.se.sopro.ui.database;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import de.cau.inf.se.sopro.R;
import de.cau.inf.se.sopro.databinding.FragmentBookBinding;
import de.cau.inf.se.sopro.model.Book;
import de.cau.inf.se.sopro.persistence.BookDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class BookFragment extends Fragment {

    private FragmentBookBinding binding;

    @Inject
    BookDatabase bookDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Book book = (Book) getArguments().get("book");

        binding = FragmentBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.textEditTitle.setText(book.getTitle());
        binding.textViewAuthorName.setText(book.getAuthor().getForename() + " " + book.getAuthor().getSurname());

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();

        binding.fab.setOnClickListener(view -> {
            book.setTitle(binding.textEditTitle.getText().toString());
            book.setNeedsSyncUpdate(true);
            bookDatabase.getBookDao().insert(book).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> {
                navController.navigateUp();
            }).subscribeOn(Schedulers.io()).subscribe(() -> {
                Log.d("SoPro", "Updated database.");
            }, throwable -> {
                Log.e("SoPro", "Could not update database.", throwable);
            });
        });

        return root;
    }

}