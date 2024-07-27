package com.example.mvvm.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mvvm.R;
import com.example.mvvm.activities.SubprojectActivity;
import com.example.mvvm.viewmodel.GeoDataViewModel;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * Adapter-Klasse zum darstellen der InfoWindows auf der Map.
 */
public class InfoWindowAdapter extends InfoWindow {

    // Teilprojekt-Name
    private String title;

    private String buttonText;

    private long sid;

    private AppCompatActivity activity;

    private String commentText;

    /**
     * Initialisieren
     *
     * @param layoutResId
     * @param mapView     Map auf der das InfoWindow dargestellt werden soll.
     */
    public InfoWindowAdapter(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    /**
     * Methode die bei dem öffnen des InfoWindows ausgeführt wird.
     *
     * @param item
     */
    @Override
    public void onOpen(Object item) {
        TextView name = mView.findViewById(R.id.info_window_text);
        Button btn = mView.findViewById(R.id.info_window_button);
        TextView comment = mView.findViewById(R.id.info_anzahl_comments);

        name.setText(title);
        btn.setText(buttonText);
        comment.setText(commentText);

        btn.setOnClickListener(new View.OnClickListener() {

            /**
             * Listener für den Weiterleitungs-Button der durch antippen die
             * jeweilige Teilprojekt-Activity startet.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                if (buttonText.equals("Zum Teilprojekt")) {
                    Intent i = new Intent(activity, SubprojectActivity.class);
                    SubprojectActivity.setSid(sid);
                    activity.startActivity(i);
                } else {
                    GeoDataViewModel geo = GeoDataViewModel.getInstance();
                    geo.calcCurrentUserPosition();
                }
            }
        });

    }

    @Override
    public void onClose() {

    }

    // *********************************************************************************************
    // Setter

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
