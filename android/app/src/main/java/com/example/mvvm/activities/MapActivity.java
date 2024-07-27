package com.example.mvvm.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mvvm.R;
import com.example.mvvm.viewmodel.AppViewModel;
import com.example.mvvm.viewmodel.GeoDataViewModel;

/**
 * Activity die eine Map enthält auf der man seine Position sowie die Positionen der Teilprojekte
 * des aktuellen Projekts über Marker feststellen kann. Außerdem bietet sie die Möglickkeit über
 * das antippen eines Buttons im InfoWindow des Teilprojekts auf die jeweilige Teilprojektseite
 * weitegeleitet werden zu können.
 */
public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        GeoDataViewModel geo = GeoDataViewModel.getInstance();
        geo.setActivity(this);

        // Initialisieren der Karte
        geo.initModel(AppViewModel.getSubprojectListByPID(ProjectdetailsActivity.getPid()));

    }
}
