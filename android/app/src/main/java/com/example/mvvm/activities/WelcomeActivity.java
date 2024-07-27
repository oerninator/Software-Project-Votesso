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
import android.widget.Toast;

import com.example.mvvm.R;
import com.example.mvvm.viewmodel.AppViewModel;

/**
 * Activity die nur zum erstmaligen Starten der App aufgerufen werden soll. Hier wird der App-Nutzer
 * begrüßt und kann einen Namen eingeben unter dem er in der Kommentarsektionen seine Kommentare
 * besser finden kann.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // *****************************************************************************************
        // Entfernen der Leiste im oberen Bildrand
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        // *****************************************************************************************

        setContentView(R.layout.activity_welcome);

        AppViewModel appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        Button saveButton = findViewById(R.id.save_button_welcome_ID);
        EditText nameView = findViewById(R.id.name_text_view_welcome_ID);

        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Listener für den Speichern-Button. Durch antippen wird der eingegeben User
             * in der Datenbank angelegt.
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                String eingabe = nameView.getText().toString();
                if(!eingabe.equals("")){
                    appViewModel.insertUser(getApplicationContext(), eingabe);
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(WelcomeActivity.this,"Bitte gib zuerst einen Namen ein.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}