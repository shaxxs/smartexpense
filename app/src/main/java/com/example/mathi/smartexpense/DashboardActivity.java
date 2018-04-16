package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /* récupération des données de la vue précédente */
        Intent intent = getIntent();
        SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        JSONObject userProfile = null;
        try {
            userProfile = new JSONObject(user_profile);
            Log.v("Data SharedPreferences", userProfile.getString("email") + "/" + userProfile.getString("nom"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView firstName= findViewById(R.id.labelFirstName);
        TextView lastName= findViewById(R.id.labelLastName);
        TextView labelEmail= findViewById(R.id.labelEmail);
        try {
            firstName.setText(userProfile.getString("userFirstName"));
            lastName.setText(userProfile.getString("userLastName").toUpperCase());
            labelEmail.setText(userProfile.getString("userEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Gestion du clic sur le bouton Mes notes de frais */
        Button bouton = (Button) findViewById(R.id.expenseReportButton);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Lien vers la vue Mes notes de frais */
                Intent intent1 = new Intent(DashboardActivity.this, ExpenseReportActivity.class);
                startActivity(intent1);
            }
        });

        /* Gestion du clic sur le bouton Suivi remboursements */
        Button bouton1 = (Button) findViewById(R.id.refundTrackerButton);
        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /* Lien vers la vue Suivi des remboursements */
                Intent intent2 = new Intent(DashboardActivity.this, RefundTrackerActivity.class);
                startActivity(intent2);
            }
        });

        /* Gestion du clic sur le bouton Statistiques */
        Button bouton2 = (Button) findViewById(R.id.statButton);
        bouton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /* Lien vers la vue Statistiques */
                Intent intent3 = new Intent(DashboardActivity.this, StatsActivity.class);
                startActivity(intent3);
            }
        });          
    }
}
