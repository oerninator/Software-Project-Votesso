package de.cau.inf.se.sopro.ui.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import de.cau.inf.se.sopro.R;
import de.cau.inf.se.sopro.databinding.FragmentDatabaseBinding;
import de.cau.inf.se.sopro.ui.search.BookAdapter;

@AndroidEntryPoint
public class DatabaseFragment extends Fragment implements BookAdapter.ListItemClickListener {

    private FragmentDatabaseBinding binding;
    private NavController navController;
    private DatabaseViewModel databaseViewModel;
    private BookAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseViewModel =
                new ViewModelProvider(this).get(DatabaseViewModel.class);

        binding = FragmentDatabaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup RecyclerView
        RecyclerView recyclerView = binding.bookRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new BookAdapter(this);
        recyclerView.setAdapter(adapter);

        databaseViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooks(books);
        });

        // Initialize data via database
        databaseViewModel.loadAllBooksFromDb();

        // Setup Swipe Refresh
        binding.bookSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Update viewmodel via network request on pull down
                databaseViewModel.synchronizeAllBooks();
                binding.bookSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(root, "Synchronized with server.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        //  Setup FAB
        binding.floatingActionButton.setOnClickListener(view -> {
            // Update viewmodel via network request on pull down
            databaseViewModel.synchronizeAllBooks();
            binding.bookSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(root, "Synchronized with server.", Snackbar.LENGTH_SHORT)
                    .show();
        });

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        this.navController = navHostFragment.getNavController();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(int position) {
        Bundle payload = new Bundle();
        payload.putSerializable("book", adapter.getBookById(position));
        this.navController.navigate(R.id.action_navigation_database_to_navigation_book, payload);
    }
}