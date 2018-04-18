package com.example.mathi.smartexpense;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class NewERActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    String result;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer);

 /** RECUPERATION DONNEES VUE PRECEDENTE **/

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

/** GESTION DU CHOIX DE LA DATE **/

        //initialiser le champs date
        final TextView thedate = findViewById(R.id.date);
        // perform click event on edit text
        thedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                Locale.setDefault(Locale.FRANCE);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewERActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                thedate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


/** GESTION DU CHOIX CLIENT **/

        //Appel de la fonction pour récupérer la liste de tous les clients
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/customer";
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(myURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Retour HTTPGetRequest : " + result);
//        $customers=json_decode(result);


        //Remplissage de la spinner avec choix multiple
        final Spinner spinner = (Spinner) findViewById(R.id.clientsSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryNames, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

/** GESTION DE LA VALIDATION DE LA NOTE DE FRAIS **/

        final EditText cityText = findViewById(R.id.city);
        final EditText commentsText = findViewById(R.id.comments);

        Button buttonAddER = findViewById(R.id.btnAddExpense);
        buttonAddER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ERDate= (String) thedate.getText();
                String city = String.valueOf(cityText.getText());
                String customer = String.valueOf(spinner.getSelectedItem());
                String comments = String.valueOf(commentsText.getText());

                //Appel de la fonction pour créer une note de frais
                String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenseReport";
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    result = getRequest.execute(myURL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Retour HTTPGetRequest : " + result);
//        $customers=json_decode(result);

                /* lien vers la vue ajouter une dépense */
                Intent intent = new Intent(NewERActivity.this, NewExpenseActivity.class);
                /* Je transmets à la vue suivante l'id de la note de frais pour les relier aux dépenses */
                //intent.putExtra(expenseReportCode);
                startActivity(intent);
            }
        });

/** BOUTON RETOUR **/

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
}

