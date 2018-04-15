package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseReportActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        /* récupération des données de la vue précédente */
        SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        JSONObject userProfile = null;
        try {
            userProfile = new JSONObject(user_profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (userProfile != null) {
                Log.v("Data SharedPreferences", userProfile.getString("email") + "/" + userProfile.getString("nom"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Gestion du clic sur le bouton Ajouter une nouvelle note de frais
        Button boutonAdd = findViewById(R.id.addERButton);
        boutonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lien vers la vue Ajouter une Note de Frais
                Intent intentNewER = new Intent(ExpenseReportActivity.this, NewERActivity.class);
                startActivity(intentNewER);
            }
        });

/* Gestion du clic sur le bouton retour */
        Button boutonReturn = findViewById(R.id.returnButton);
        boutonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intent = new Intent(ExpenseReportActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
