package com.example.mvvm.viewmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.R;
import com.example.mvvm.adapter.InfoWindowAdapter;
import com.example.mvvm.model.Subproject;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Diese Klasse übernimmt jegliche Logik, die mit der Map auf der activity_projectdetails zu tun hat
 */
public class GeoDataViewModel extends ViewModel {

    private static GeoDataViewModel geo = null;
    private MapView map = null;
    private Marker currentPositionMarker;
    private AppCompatActivity activity;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    private GeoPoint currentUserPosition;
    private long sid = -1;
    private LocationRequest locationRequest;
    private String[] defaultPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION};
    private ArrayList<Marker> markers = new ArrayList<>();

    private GeoDataViewModel() {

    }

    //Singleton Entwurfsmuster
    public synchronized static GeoDataViewModel getInstance() {
        if(geo == null) {
            geo = new GeoDataViewModel();
        }
        return geo;
    }

    /**
     * setzt die activity.
     * @param activity neue activity.
     */
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * initialisiert das Model.
     * @param subprojects Liste an Subprojects die angezeigt werden soll.
     */
    public void initModel(List<Subproject> subprojects) {
        Context ctx = activity.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = activity.findViewById(R.id.map_view);
        map.getController().setZoom(12.0);

        currentPositionMarker = new Marker(map);
        setMarkerCurrentPosition();

        //Wir bestimmen die aktuelle Position des Benutzers
        calcCurrentUserPosition();

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        CompassOverlay compassOverlay = new CompassOverlay(activity, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        //Marker für die einzelnen Teilprojekte auf der Map anzeigen
        GeoPoint point = null;
        for(Subproject subproject : subprojects) {
            point = new GeoPoint(subproject.getMapLocation().getLatitude(), subproject.getMapLocation().getLongitude());
            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            InfoWindowAdapter infoWindow = new InfoWindowAdapter(R.layout.info_window_marker, map);
            infoWindow.setButtonText("Zum Teilprojekt");
            infoWindow.setTitle(subproject.getTitle());
            infoWindow.setSid(subproject.getId());
            infoWindow.setActivity(activity);
            infoWindow.setCommentText("Anzahl Kommentare: " + subproject.getCommentsList().size());

            startMarker.setInfoWindow(infoWindow);

            if(sid == subproject.getId()) {
                startMarker.showInfoWindow();
                map.getController().setCenter(point);
                map.getController().setZoom(17.0);
            }

            startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    for(Marker m : markers) {
                        m.closeInfoWindow();
                    }
                    marker.showInfoWindow();
                    return false;
                }
            });

            map.getOverlays().add(startMarker);
            markers.add(startMarker);
        }

        markers.add(currentPositionMarker);

    }

    /**
     * gibt die aktuell gesetzte sid zurück.
     * @return aktuelle sid.
     */
    public long getSid() {
        return this.sid;
    }

    /**
     * setzt die sid.
     * @param sid neue sid.
     */
    public void setSid(long sid) {
        this.sid = sid;
    }

    /**
     * gibt den Request_Permission_Request_Code zurück.
     * @return Request_Permission_Request_Code.
     */
    public int getRequestCode() {
        return this.REQUEST_PERMISSIONS_REQUEST_CODE;
    }

    /**
     * gibt unsere benötigten permissions zurück.
     * @return defaultPermissions.
     */
    public String[] getDefaultPermissions() {
        return this.defaultPermissions;
    }

    /**
     * gibt die letzte Position des Benutzers zurück.
     */
    @SuppressLint("MissingPermission")
    public void calcLastUserPosition() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    if(currentUserPosition == null) {
                        currentUserPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
                    } else {
                        currentUserPosition.setLatitude(location.getLatitude());
                        currentUserPosition.setLongitude(location.getLongitude());
                    }
                    System.out.println(location.getLatitude() + "  " + location.getLongitude());

                }
            }
        });
    }

    /**
     * Öffnet die GPS Einstellungsseite
     */
    public void turnGPSOnManually(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * berechnet die aktuelle Position des Benutzers.
     */
    public void calcCurrentUserPosition() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getCurrentLocation();
    }

    /**
     * führt die eigentliche Berechnung der Position durch.
     */
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions(defaultPermissions)) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(activity)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(activity)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        setCurrentUserPosition(new GeoPoint(latitude, longitude));
                                        setMarkerCurrentPosition();
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    switchGPSOn();
                }
            } else {
                this.requestPermissions(defaultPermissions);
            }
        }
    }

    /**
     * Überprüft ob die Permissions vorhanden sind.
     * @param permissions Permissions die überprüft werden sollen.
     * @return true für alle permissions vorhanden und false für mindestens eine nicht vorhanden.
     */
    public boolean checkPermissions(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fragt die Permissions ab.
     * @param permissions Permissions die angefragt werden sollen.
     */
    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this.activity, permissions, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    /**
     * setllt die GPS Funktion an.
     */
    public void switchGPSOn() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(activity, "GPS wurde erfolgreich aktiviert!", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(activity, "GPS konnte nicht aktiviert werden!", Toast.LENGTH_LONG).show();

                if(e instanceof ResolvableApiException) {

                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(activity, REQUEST_PERMISSIONS_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * Überprüft ob die GPS Funktion aktiviert ist.
     * @return gibt true für an und false für aus an.
     */
    public boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    /**
     * Setzt die Position des Benutzers.
     * @param currentUserPosition neue Benutzer Position.
     */
    public void setCurrentUserPosition(GeoPoint currentUserPosition) {
        this.currentUserPosition = currentUserPosition;
    }


    /**
     * Setzt einen Marker auf die Position des Benutzers.
     */
    public void setMarkerCurrentPosition() {
        //currentPositionMarker = new Marker(map);
        InfoWindowAdapter infoWindow = new InfoWindowAdapter(R.layout.info_window_marker, map);

        infoWindow.setButtonText("Position Neu bestimmen");
        infoWindow.setTitle("Aktuelle position");
        infoWindow.setActivity(activity);
        infoWindow.setCommentText("");

        currentPositionMarker.setInfoWindow(infoWindow);

        currentPositionMarker.setPosition(currentUserPosition);


        currentPositionMarker.setIcon(activity.getResources().getDrawable(R.drawable.ic_baseline_location_on_24));

        if(sid == -1) {
            currentPositionMarker.showInfoWindow();
            map.getController().setCenter(currentUserPosition);
        }

        map.getOverlays().add(currentPositionMarker);
    }

    /**
     * Gibt die Position des Benutzers zurück.
     * @return Position des Benutzers.
     */
    public GeoPoint getCurrentUserPosition() {
        return this.currentUserPosition;
    }

    /**
     * Sortiert eine Liste von Teilprojekten nach Geodaten.
     * @param point Referenzpunkt für die Distanzbestimmung.
     * @param subprojects Liste von Teilprojekten.
     * @return sortierte Liste nach Geodaten.
     */
    public List<Subproject> getSortedList(GeoPoint point, List<Subproject> subprojects) {
        if(point == null) {
            //Falls das passiert, wurde die Position nicht bestimmt
            System.err.println("Fehler, Position des Benutzers wurde nicht bestimmt");
            return subprojects;
        }
        double[] distances = new double[subprojects.size()];

        //Entfernung zu jedem Teilprojekt bestimmen
        for(int i = 0; i < distances.length; i++) {
            Subproject s = subprojects.get(i);
            distances[i] = calcDistance(point, new GeoPoint(s.getMapLocation().getLatitude(), s.getMapLocation().getLongitude()));
        }


        Subproject[] s = new Subproject[subprojects.size()];

        for(int i = 0; i < subprojects.size(); i++) {
            s[i] = subprojects.get(i);
        }

        //Nach Distanz sortieren, Bubblesort reicht hier aus
        double temp;
        Subproject tempS;
        for(int i = 1; i < distances.length; i++) {
            for(int j = 0; j < distances.length-i; j++) {
                if(distances[j] > distances[j+1]) {
                    temp = distances[j];
                    distances[j] = distances[j+1];
                    distances[j+1] = temp;
                    tempS = s[j];
                    s[j] = s[j+1];
                    s[j+1] = tempS;
                }

            }
        }

        return Arrays.asList(s);
    }


    //(111.3 = Abstand zwischen zwei Breitenkreisen in km)
    //71.5 der durchschnittliche Abstand zwischen zwei Längenkreisen in unseren Breiten.
    /**
     * Berechnet die Distanz zwischen zwei Punkten.
     * @param point1 Erster Punkt.
     * @param point2 Zweiter Punkt.
     * @return gibt die Distanz zwischen den beiden Punkten zurück.
     */
    public double calcDistance(GeoPoint point1, GeoPoint point2) {
        double dx = 71.5 * (point1.getLongitude() - point2.getLongitude());
        double dy = 111.3 * (point1.getLatitude() - point2.getLatitude());
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
    }

}
