package com.example.mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvvm.R;
import com.example.mvvm.adapter.SubprojectsOverviewAdapter;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.viewmodel.AppViewModel;
import com.example.mvvm.viewmodel.GeoDataViewModel;

import java.util.List;

/**
 * Activity die die Projektdaten darstellt. Über die Liste der Teilprojekte kann man entweder auf
 * die Teilprojekt Seiten weitergeleitet werden oder auf die Map, auf der man die Teilprojekte
 * über Pin-Marker finden kann.
 */
public class ProjectdetailsActivity extends AppCompatActivity {

    //Wird genutzt um der Recyclerview die Teilprojekte zu übergeben
    private List<Subproject> subprojects;

    private static long pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // *****************************************************************************************
        // Entfernen der Leiste im oberen Bildrand
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        // *****************************************************************************************

        setContentView(R.layout.activity_projectdetails);

        // *****************************************************************************************
        // RecyclerView-Code zum Anzeigen der Teilprojekte auf der Projektdetailseite

        RecyclerView recyclerView = findViewById(R.id.recycler_view_subprojects);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        SubprojectsOverviewAdapter adapter = new SubprojectsOverviewAdapter(this, subprojects);
        recyclerView.setAdapter(adapter);

        //Hier wird der letzte Standort des Users bestimmt, um nach Geodaten zu sortieren
        GeoDataViewModel geo = GeoDataViewModel.getInstance();
        geo.setActivity(this);
        geo.calcLastUserPosition();


        // *****************************************************************************************
        // OnClick Listener für den 'Karte anschauen' Button
        findViewById(R.id.zeige_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProjectdetailsActivity.this, MapActivity.class);
                geo.setSid(-1);
                ProjectdetailsActivity.this.startActivity(i);
            }
        });

        // *****************************************************************************************
        //  Zurück Button
        findViewById(R.id.back_arrow_projectdetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProjectdetailsActivity.this, MainActivity.class);
                ProjectdetailsActivity.this.startActivity(i);
            }
        });

        // *****************************************************************************************
        // Activity-Code: Füllen mit Daten des ausgewählten Projekts

        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Sammeln aller benötigten IDs der verwendeten Views
        TextView tv = findViewById(R.id.textView5);
        ImageView im = findViewById(R.id.imageViewProjectDetailsID);
        TextView tv_phase = findViewById(R.id.text_view_phase_text_ID);
        TextView tv_info = findViewById(R.id.text_view_project_info_ID);

        AppViewModel.getProjects();

        // Füllen der Views mit projektspezifischen Daten
        appViewModel.getProjectList().observe(this, new androidx.lifecycle.Observer<List<Project>>() {
            /**
             * Beim aktualiseiren der LiveData im AppViewModel wird die Activity über diese
             * Methode ebenfalls aktualisiert.
             *
             * @param projects
             */
            @Override
            public void onChanged(List<Project> projects) {
                Project project = AppViewModel.getProjectByPID(pid);
                tv.setText(" " + project.getTitle() + " ");
                Glide.with(im).load(project.getImage()).into(im);
                tv_phase.setText(project.getPhase());
                tv_info.setText(project.getDescription());
                subprojects = project.getSubprojects();

                // Übergibt der Recyclerview die Daten und die id des Projekts
                adapter.setSubprojects(geo.getSortedList(geo.getCurrentUserPosition(), subprojects));
                adapter.setCurrentProject(pid);

            }
        });

        // Holen neuer Daten um die 'onChanged()'-Methode zu triggern
        appViewModel.getData();

        // *****************************************************************************************
    }

    /**
     * Getter für die Projekt-ID
     *
     * @return
     */
    public static long getPid() {
        return pid;
    }

    /**
     * Setter für die Projekt-ID
     *
     * @param pid
     */
    public static void setPid(long pid) {
        ProjectdetailsActivity.pid = pid;
    }

}