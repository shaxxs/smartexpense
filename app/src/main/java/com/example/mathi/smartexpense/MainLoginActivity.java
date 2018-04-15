package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathi.smartexpense.model.ExpenseReportAdapter;
import com.example.mathi.smartexpense.network.HttpGetRequest;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import android.widget.Toast;

public class MainLoginActivity extends AppCompatActivity {
    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        final EditText login = findViewById(R.id.inputLoginEmail);
        final EditText pass = findViewById(R.id.inputLoginPassword);

        /* Gestion click sur le bouton */
        Button bouton = findViewById(R.id.btnConnexion);
        bouton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String myURL = "http://www.gyejacquot-pierre.fr/loggin.php?email=" +
                        login.getText().toString() + "&mdp=" + pass.getText().toString();
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    result = getRequest.execute(myURL).get();
                    System.out.println("Retour HTTPGetRequest : " + result);
                    if (result.equals("")){
                        //afficher un message pour avertir de l'échec de la connexion
                        Context context = getApplicationContext();
                        CharSequence text = "Mot de Passe ou identifiant incorrect !";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }else {
                        //on ajoute les identifiants à notre fichier sharedpreferences
                        SharedPreferences myPref = getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putString(LOGIN_PASS_KEY, result);
                        editor.apply();
                        Log.v("sharedpreferences", myPref.getString(LOGIN_PASS_KEY, "{}"));
                        //afficher un message pour avertir de la réussite de la connexion
                        Context context = getApplicationContext();
                        CharSequence text = "Connexion Réussie";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent intent = new Intent(MainLoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

