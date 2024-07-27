package com.example.mvvm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm.R;
import com.example.mvvm.adapter.CommentViewAdapter;
import com.example.mvvm.model.Comment;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity, die vom Backend gezogene Kommentare anzeigt. Die Kommentare werden mit einem Timestamp und mittels einer Recyclerview dargestellt.
 */

public class CommentActivity extends AppCompatActivity {
    private ArrayList<Comment> comments;
    private RecyclerView recyclerView;
    private Intent intent;
    private AppViewModel viewModel;
    private TextView headline;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        // Sammeln aller benötigten IDs der verwendeten Views
        recyclerView = findViewById(R.id.recycler_view_comments);
        headline = findViewById(R.id.commentTextID);

        //holt die ID des Subprojekts
        long sid = SubprojectActivity.getSid();

        //Setze die überschrift der Comment-Activity
        headline.setText("Kommentare zu Projekt\n" + AppViewModel.getProjectByPID(ProjectdetailsActivity.getPid()).getTitle() + ", " + AppViewModel.getSubprojectBySID(SubprojectActivity.getSid()).getTitle());

        comments = new ArrayList<>();

        //**********************************************************************************************************************************************************************

        AppViewModel appViewModel = new ViewModelProvider(CommentActivity.this).get(AppViewModel.class);

        //**********************************************************************************************************************************************************************

        // Füllen der Views mit projektspezifischen Daten...
        // Setze einen Observer auf die Livedaten im ViewModel
        appViewModel.getProjectList().observe(this, new androidx.lifecycle.Observer<List<Project>>() {

            /**
             * Wenn sich die LiveDaten ändern, wird die Methode onChanged(List<Project> projects) getriggert und
             * die Views angepasst.
             */
            @Override
            public void onChanged(List<Project> projects) {
                Subproject subproject = AppViewModel.getSubprojectBySID(sid);
                comments = (ArrayList<Comment>) subproject.getCommentsList();
                Collections.reverse(comments);
                setAdapter();

            }
        });
        // Holen neuer Daten um die 'onChanged()'-Methode zu triggern
        appViewModel.getData();


    }

    /**
     * Initialisert den Adapter für die Recyclerview und fügt diesen der Recyclerview hinzu
     */
    private void setAdapter() {
        CommentViewAdapter adapter = new CommentViewAdapter(comments);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
