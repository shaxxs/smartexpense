package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

public class NewERActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer);

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

        /*//Remplissage de la spinner avec choix multiple
        Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryNames, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/

        /* Gestion du clic sur le bouton retour */
        Button boutonReturn = findViewById(R.id.returnButton);
        boutonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Lien vers la vue Notes de Frais */
                Intent intent = new Intent(NewERActivity.this, ExpenseReportActivity.class);
                startActivity(intent);
            }
        });

    }
//gestion du clic sur la spinner pour la categorie
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Another interface callback
    }

    //Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
    //spinner.setOnItemSelectedListener(this);
}

