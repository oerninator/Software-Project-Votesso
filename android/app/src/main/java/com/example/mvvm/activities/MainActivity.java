package com.example.mvvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm.R;
import com.example.mvvm.adapter.ProjectsOverviewAdapter;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.User;
import com.example.mvvm.viewmodel.AppViewModel;
import com.example.mvvm.viewmodel.GeoDataViewModel;

import java.util.List;


/**
 * Bei Starten der App wird die MainActivity  gestartet. Auf der Mainactivity werden die Hauptprojekte (z.B. Tram Kiel oder Fahrradstadt München) mittels einer Recyclerview angezeigt.
 */
public class MainActivity extends AppCompatActivity {


    private List<Project> projectModelList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GeoDataViewModel geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //****************************************************************************************

        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        //****************************************************************************************

        //Direkt beim Start der App werden nur die Permissons geholt
        geo = GeoDataViewModel.getInstance();
        geo.setActivity(this);
        if(! geo.checkPermissions(geo.getDefaultPermissions())) {
            geo.requestPermissions(geo.getDefaultPermissions());
        }

        // *****************************************************************************************
        // Database stuff

        // Auskommentieren und App starten, schließen, wieder kommentieren und starten um DB zu löschen
        //appViewModel.clearDB(getApplicationContext());

        User user = appViewModel.getUser(getApplicationContext());


        if(user == null){
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }else {
            User u = appViewModel.getUser(getApplicationContext());
            if(u.getVoteIDs().isEmpty()){
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }else {
                String name = u.getName();
                AppViewModel.setUserName(name);
                AppViewModel.setUserID((int) u.getVoteIDs().get(0).longValue());
            }
        }


        // *****************************************************************************************

        // Um oben die Leiste zu entfernen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        // *****************************************************************************************
        // RecyclerView-Code zum Anzeigen der Projekte auf der Hauptseite

        RecyclerView recyclerView = findViewById(R.id.recylerViewID);

        TextView noInternet = findViewById(R.id.noInternetID);

        LinearLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        ProjectsOverviewAdapter adapter = new ProjectsOverviewAdapter(this, projectModelList);
        recyclerView.setAdapter(adapter);

        //GeoDataViewModel.requestPermissionsIfNecessary(this);

        // *****************************************************************************************
        // Swipe-Refresh-Code

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> reloadPage());

        // *****************************************************************************************
        // Activity-Code: Füllen der Activity/RecyclerView mit aktuellen Daten

        // Übergeben der Daten an die RecyclerView
        appViewModel.getProjectList().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                if(projects != null){
                    projectModelList = projects;
                    adapter.setProjectModelList(projects);
                    noInternet.setVisibility(View.GONE);
                }else{
                    noInternet.setVisibility(View.VISIBLE);
                }
            }
        });

        // Holen neuer Daten um die 'onChanged()'-Methode zu triggern
        appViewModel.getData();

        // *****************************************************************************************
        // Settings Button
        View settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ServersettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Beim nach unten wischen vom oberen Bildrand
     */
    public void reloadPage(){
        finish();
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        swipeRefreshLayout.setRefreshing(false); //Nicht unbedingt nötig, da ganze Activity neu geladen wird
    }


    @Override
    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == geo.getRequestCode()) {
            if(geo.checkPermissions(permissions)) {
                if(!geo.isGPSEnabled()) {
                    geo.switchGPSOn();
                }
            }
        }
    }

}