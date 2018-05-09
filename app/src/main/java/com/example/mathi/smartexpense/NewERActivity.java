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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class NewERActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    String result,result2;
    JSONObject userProfile = null;
    int idUser;
    Spinner clientsSpinner;
    String customerSelected;
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String EXPENSE_REPORT_DATE = "expense_report_date";
    final String EXPENSE_REPORT_CITY = "expense_report_city";
    final String EXPENSE_REPORT_SUBMISSION_DATE = "expense_report_submission_date";
    private String currentDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer);

        //Gestion de la liste déroulante
        addListenerOnSpinnerItemSelection();

 /** RECUPERATION DONNEES VUE PRECEDENTE **/

        final SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        final String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        try {
            userProfile = new JSONObject(user_profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (userProfile != null) {
                Log.v("Data SharedPreferences", userProfile.getString("userLastName") + "/" + userProfile.getString("userFirstName"));
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
                currentDate = String.valueOf(mYear+ "-"+(mMonth+1)+"-"+mDay);
                Locale.setDefault(Locale.FRANCE);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewERActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                thedate.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

/** GESTION DU CHOIX CLIENT **/

        //Appel de la fonction pour récupérer la liste de tous les clients
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/customers";
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(myURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Retour HTTPGetRequest Customer: " + result);


        //Remplissage de la spinner avec choix multiple
        final Spinner spinner = (Spinner) findViewById(R.id.clientsSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryLabels, android.R.layout.simple_spinner_item);
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

                int customer = 1;
//                int customer = (int) spinner.getSelectedItem();
                String comments = String.valueOf(commentsText.getText());
                try {
                    idUser = userProfile.getInt("idUser");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    // si la date saisie est supérieure à la date du jour
                    if ((!String.valueOf(ERDate).equals("")) && parseDate(currentDate).before(parseDate(ERDate))) {
                        Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                    } else {
                        // si les champs date et ville sont vides
                        if (String.valueOf(ERDate).equals("") || String.valueOf(city).equals("")) {
                            Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        } else {
                            //Appel de la fonction pour créer une note de frais
                            String myURL2="http://www.gyejacquot-pierre.fr/API/public/expensereport/add?expenseReportDate="+ERDate+"&expenseReportCity="+city+"&expenseReportComment="+comments+"&idUser="+idUser+"&idCustomer="+customer;
                            //String myURL2 = "http://10.0.2.2/smartExpenseApi/API/public/expensereport/add?expenseReportDate=" + ERDate + "&expenseReportCity=" + city + "&expenseReportComment=" + comments + "&idUser=" + idUser + "&idCustomer=" + customer;

                            HttpGetRequest getRequest = new HttpGetRequest();
                            try {
                                result2 = getRequest.execute(myURL2).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Retour HTTPGetRequest ExpenseReport : " + result2);


                    /* lien vers la vue ajouter une dépense */
                            Intent intent = new Intent(NewERActivity.this, NewExpenseActivity.class);
                    /* Je transmets à la vue suivante les données de la note de frais pour les relier aux dépenses */
                            startActivity(intent);
                            String submissionDate = "";
                            SharedPreferences sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, Context.MODE_PRIVATE);
                            sharedPreferencesER.edit()
                                    .putString(EXPENSE_REPORT_CITY, city)
                                    .putInt(EXPENSE_REPORT_CODE, Integer.parseInt(result2))
                                    .putString(EXPENSE_REPORT_DATE, ERDate)
                                    .putString(EXPENSE_REPORT_SUBMISSION_DATE, submissionDate)
                                    .apply();
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

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

    //Gestion de la sélection sur la liste déroulante
    private void addListenerOnSpinnerItemSelection() {
        clientsSpinner = (Spinner) findViewById(R.id.clientsSpinner);
        clientsSpinner.setOnItemSelectedListener(this);
    }

//Gestion de l'item sélectionné dans la liste déroulante
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        customerSelected = adapterView.getItemAtPosition(i).toString();
        System.out.println("Client selectionne: "+customerSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Another interface callback
    }

    /** fonction qui parse du String en Date */
    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);
        return newDate;
    }
}

