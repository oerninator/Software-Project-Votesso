package com.example.mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvvm.R;
import com.example.mvvm.model.Project;
import com.example.mvvm.model.Subproject;
import com.example.mvvm.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Activity die Teilprojekte darstellt. Hier kann der Nutzer einen Vote abgeben,
 * Teilprojektinformationen einsehen, einen Kommentar eingeben und durch einen Button auf
 * die Kommentar-Activity weitergeleitet werden.
 */
public class SubprojectActivity extends AppCompatActivity {

    // Teilprojekt-ID
    private static long sid;

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

        setContentView(R.layout.activity_subproject);

        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // *****************************************************************************************
        // Activity-Code: Füllen mit Daten des ausgewählten Projekts

        // Sammeln aller benötigten IDs der verwendeten Views
        TextView tv_title = findViewById(R.id.text_view_subproject_title_ID);
        ImageView im = findViewById(R.id.imageViewSubprojectDetailsID);
        TextView tv_price = findViewById(R.id.text_view_subproject_price_text_ID);
        TextView tv_info = findViewById(R.id.text_view_subproject_info_ID);
        TextView tv_thumbUp = findViewById(R.id.text_view_thumb_up_ID);
        TextView tv_thumbDown = findViewById(R.id.text_view_thumb_down_ID);

        // Füllen der Views mit projektspezifischen Daten...
        //Setze einen Observer auf die Livedaten im ViewModel
        appViewModel.getProjectList().observe(this, new androidx.lifecycle.Observer<List<Project>>() {

            /**
             * Wenn sich die LiveDaten ändern, wird die Methode onChanged(List<Project> projects) getriggert und
             * die Views angepasst.
             * @param projects
             */
            @Override
            public void onChanged(List<Project> projects) {

                Subproject subproject = AppViewModel.getSubprojectBySID(sid);

                tv_title.setText(" " + subproject.getTitle() + " ");
                Glide.with(im).load(subproject.getImage()).into(im);
                tv_price.setText((int) subproject.getPrice() + " €");
                tv_info.setText(subproject.getDescription());

                FloatingActionButton thumbDown = findViewById(R.id.thumb_down_ID);
                FloatingActionButton thumbUp = findViewById(R.id.thumb_up_ID);

                // Wenn der Nutzer für dieses Teilprojekt bereits gevotet hat, werden
                // die Anzeigen entsprechend angepasst.
                if (appViewModel.userCheckSubprojectVoted(getApplicationContext(), sid)) {
                    thumbUp.setClickable(false);
                    thumbDown.setClickable(false);
                    thumbUp.setColorFilter(R.color.thumbGrey);
                    thumbDown.setColorFilter(R.color.thumbGrey);

                    tv_thumbUp.setVisibility(View.VISIBLE);
                    tv_thumbDown.setVisibility(View.VISIBLE);
                    tv_thumbUp.setText(AppViewModel.getVoteUpCount(sid) + "");
                    tv_thumbDown.setText(AppViewModel.getVoteDownCount(sid) + "");
                }
            }
        });

        // Holen neuer Daten um die 'onChanged()'-Methode zu triggern
        appViewModel.getData();
        // *****************************************************************************************


        //Setzt einen Listener auf den Zurück-Button (oben links)
        findViewById(R.id.back_arrow_subproject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubprojectActivity.this, ProjectdetailsActivity.class);
                startActivity(i);
            }
        });


        // *****************************************************************************************
        // Vote

        FloatingActionButton v_thumbUp = findViewById(R.id.thumb_up_ID);
        FloatingActionButton v_thumbDown = findViewById(R.id.thumb_down_ID);

        //Setzt einen Listener auf den Daumhoch-Button
        v_thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appViewModel.clickThumbUp(getApplicationContext());
            }
        });

        //Setzt einen Listener auf den Daumrunter-Button
        v_thumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appViewModel.clickThumbDown(getApplicationContext());
            }
        });


        EditText commentInput = findViewById(R.id.editTextTextMultiLine);
        Button commentButton = findViewById(R.id.button_comment_ID);

        //Setzt einen Listener auf den Kommentier-Button.
        commentButton.setOnClickListener(new View.OnClickListener() {

            // Wenn der Kommentier-Button angetippt wird, wird der Textfeldstring abgefragt. Der vom User
            // eingegebene String muss mindestens ein Zeichen enthalten

            /**
             * Wenn der Kommentier-Button angetippt wird, wird der Textfeldstring abgefragt.
             * Der vom User eingegebene String muss mindestens ein Zeichen enthalten
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (!(commentInput.getText().toString().isEmpty())) {

                    //Nötige Daten für ein Comment-Objekt werden dem AppViewModel bereit gestellt
                    appViewModel.sentComment(getApplicationContext(), SubprojectActivity.sid, commentInput.getText().toString());
                    Toast.makeText(SubprojectActivity.this, "Vielen Dank für ihr Feedback!", Toast.LENGTH_LONG).show();
                    commentInput.setText("");

                } else {
                    Toast.makeText(SubprojectActivity.this, "Der Kommentar muss mindestens ein Zeichen aufweisen!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * In dem LayoutFile definierte onClick() methode, die die CommentActivity startet
     *
     * @param view die View des Buttons
     */
    public void viewComments(View view) {
        Intent i = new Intent(this, CommentActivity.class);
        startActivity(i);
    }

    /**
     * Getter für die sid
     *
     * @return sid
     */
    public static long getSid() {
        return sid;
    }

    /**
     * Setter für die SID
     *
     * @param sid
     */
    public static void setSid(long sid) {
        SubprojectActivity.sid = sid;
    }

}
