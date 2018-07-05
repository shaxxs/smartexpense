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

import com.example.mathi.smartexpense.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class DashboardActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    private User user = new User();

    TextView firstName;
    TextView lastName;
    TextView labelEmail;
    Button boutonNF;
    Button bouton1;
    Button bouton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firstName = findViewById(R.id.labelFirstName);
        lastName = findViewById(R.id.labelLastName);
        labelEmail = findViewById(R.id.labelEmail);
        boutonNF = findViewById(R.id.expenseReportButton);
        bouton1 = findViewById(R.id.refundTrackerButton);
        bouton2 = (Button) findViewById(R.id.statButton);

        /** Récupération des données du fichier SharedPreferences */
        SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        JSONObject userProfile = null;
        try {
            userProfile = new JSONObject(user_profile);
            user = user.jsonToUser(userProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userProfile != null) {
            Log.v("Data SharedPreferences", user.getUserLastName() + "/" + user.getUserFirstName());
        }

        if (userProfile != null) {
            firstName.setText(user.getUserFirstName());
            lastName.setText(user.getUserLastName().toUpperCase());
            labelEmail.setText(user.getUserEmail());
        }
      
        /** Gestion du clic sur le bouton Mes notes de frais */
        boutonNF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lien vers la vue Mes notes de frais
                Intent intent1 = new Intent(DashboardActivity.this, ExpenseReportActivity.class);
                startActivity(intent1);
            }
        });

        /** Gestion du clic sur le bouton Suivi remboursements */
        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lien vers la vue Suivi des remboursements
                Intent intent2 = new Intent(DashboardActivity.this, RefundTrackerActivity.class);
                startActivity(intent2);
            }
        });

        /** Gestion du clic sur le bouton Statistiques */
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
