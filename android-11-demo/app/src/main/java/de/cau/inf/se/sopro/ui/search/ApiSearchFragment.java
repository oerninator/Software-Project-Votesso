package de.cau.inf.se.sopro.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;
import de.cau.inf.se.sopro.databinding.FragmentApiSearchBinding;

@AndroidEntryPoint
public class ApiSearchFragment extends Fragment implements BookAdapter.ListItemClickListener {

    private FragmentApiSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiSearchViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ApiSearchViewModel.class);

        binding = FragmentApiSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText textEdit = binding.editTextTextPersonName;

        textEdit.setOnEditorActionListener((v, id, event) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                dashboardViewModel.lookupBooksByIsbn(v.getText().toString());
            }
            return false;
        });

        RecyclerView recyclerView = binding.bookRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setHasFixedSize(true);

        BookAdapter adapter = new BookAdapter(this);
        recyclerView.setAdapter(adapter);

        dashboardViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> adapter.setBooks(books));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(int position) {
        // do nothing
    }
}