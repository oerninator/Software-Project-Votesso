package com.example.mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm.R;
import com.example.mvvm.network.RetrofitInstance;
import com.example.mvvm.viewmodel.AppViewModel;

/**
 * Activity die über das Zahnrad in der oberen rechten Ecke der MainActivity erreicht werden kann.
 * Hier können einige Einstellungen vorgenommen werden.
 */
public class ServersettingsActivity extends AppCompatActivity {

    // Variablen um die genutzten Views zu halten.
    private Button safeButton;
    private EditText textInput;
    private TextView currentBackendAdressText;
    private Button defaultButton;
    private TextView defaultBackendAdressText;
    private Button buttonClearDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serversettings);

        safeButton = findViewById(R.id.speichernButtonID);
        textInput = findViewById(R.id.textInputAdressID);
        currentBackendAdressText = findViewById(R.id.currentBackendAdressID);
        defaultButton = findViewById(R.id.defaultButtonID);
        defaultBackendAdressText = findViewById(R.id.defaultValueTextID);
        buttonClearDB = findViewById(R.id.button_clear_db);

        showCurrentBackendAdress();
        showDefaultBackendAdress();




        /**
         * Setzt einen Listener auf den Speichern Button. Beim Tippen auf den Speichern Button wird die
         * eingegebene URL überprüft.
         */
        safeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RetrofitInstance.getBaseURL().equals(textInput.getText().toString())) {

                    //eine eingebene URL ist nur gültig, wenn sie mit https:// oder http:// startet
                    if(textInput.getText().toString().startsWith("https://") ||
                            textInput.getText().toString().startsWith("http://")){

                    //bei einer eingegebenen gültigen URL wird die URL neu gesetzt
                    AppViewModel.changeBaseURLAVM(textInput.getText().toString());
                    showCurrentBackendAdress();

                    Toast.makeText(ServersettingsActivity.this,
                            "Die Adresse des Backend wurde gespeichert.",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(ServersettingsActivity.this,
                                "Ungültig! Die URL muss mit https:// ...  oder http:// ... beginnen.",
                                Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        /**
         * Setzt einen Listener auf den Datenbank-Clearbutton
         */
        buttonClearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppViewModel.clearDB(getApplicationContext());
            }
        });


        /**
         * Setzt einen Listener auf den Default Button. Beim Tippen auf den Default Button wird die Basis URL auf den Default
         * Wert zurückgesetzt (eingestellter Default Wert ist die Basis-URL der VM des Rechenzentrums).
         */
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppViewModel.changeBaseURLAVM(RetrofitInstance.getDefaultBaseURL());
                showCurrentBackendAdress();
                Toast.makeText(ServersettingsActivity.this, "Der Default Wert wurde gesetzt und gespeichert!",Toast.LENGTH_SHORT).show();
            }
        });



    }

    /**
     * Setzt ein Textfeld auf die Default Backend-Adresse
     */
    private void showDefaultBackendAdress() {
        defaultBackendAdressText.setText("Default Backend-Adresse: " + RetrofitInstance.getDefaultBaseURL());
    }

    /**
     *  Setzt ein Textfeld auf die aktuelle Backend-Adresse
     */
    private void showCurrentBackendAdress() {
        currentBackendAdressText.setText("Die aktuell gespeicherte Adresse ist: \n" + RetrofitInstance.getBaseURL().toString());
    }


}