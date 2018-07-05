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


import com.example.mathi.smartexpense.model.Customer;
import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.model.User;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class NewERActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    private JSONObject userProfile = null;
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String EXPENSE_REPORT_KEY = "expense_report";

    private User user = new User();
    private ExpenseReport expenseReport;
    Customer customer;
    Customer customerSelected;

    Spinner clientsSpinner;
    Button buttonAddER;
    Button buttonReturn;
    private TextView thedate;
    private EditText cityText;
    private EditText commentsText;

    private List<Customer> customers = new ArrayList<Customer>();
    int idCustomerSelected;
    String result,result2;
    private String currentDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newer);

        buttonAddER = findViewById(R.id.btnAddExpense);
        buttonReturn = findViewById(R.id.returnButton);
        thedate = findViewById(R.id.date);
        cityText = findViewById(R.id.city);
        commentsText = findViewById(R.id.comments);
        clientsSpinner = (Spinner) findViewById(R.id.clientsSpinner);

        //Gestion de la liste déroulante
        addListenerOnSpinnerItemSelection();

        /** RECUPERATION DONNEES VUE PRECEDENTE **/

        final SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        final String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        try {
            userProfile = new JSONObject(user_profile);
            user = user.jsonToUser(userProfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (userProfile != null) {
            Log.v("Data SharedPreferences", user.getUserLastName() + "/" + user.getUserFirstName());
        }

        /** GESTION DU CHOIX DE LA DATE **/

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
                                thedate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
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
            // tableau JSON qui contient le résultat
            JSONArray array = new JSONArray(result);
            // boucle sur la longueur du tableau JSON qui contient le résultat de la requete
            for (int i= 0; i < array.length(); i++) {
                // à chaque tour de boucle, on récupère un objet JSON du tableau, qui contient les données d'un client
                JSONObject obj = new JSONObject(array.getString(i));
                // on instancie un Customer, qu'on ajoute à une liste de Customer
                customers.add(customer = new Customer(obj.getInt("idCustomer"), obj.getString("customerLastName"), obj.getString("customerFirstName")));
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("Retour HTTPGetRequest Customer: " + result);

        ArrayAdapter<Customer> adapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, customers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientsSpinner.setAdapter(adapter);

        /** GESTION DE LA VALIDATION DE LA NOTE DE FRAIS **/
        buttonAddER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String date = "";
            try {
                date = parseDateToUS(String.valueOf(thedate.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                // si la date saisie est supérieure à la date du jour
                if ((!date.equals("")) && parseDate(currentDate).before(parseDate(date))) {
                    Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                } else {
                    // si les champs date et ville sont vides
                    if (date.equals("") || String.valueOf(cityText.getText()).equals("")) {
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                    } else {
                        expenseReport = new ExpenseReport(date, String.valueOf(cityText.getText()), String.valueOf(commentsText.getText()),"", user.getIdUser(), idCustomerSelected);
                        //Appel de la fonction pour créer une note de frais
                        String myURL2="http://www.gyejacquot-pierre.fr/API/public/expensereport/add?expenseReportDate="+parseDateToUS(expenseReport.getExpenseReportDate())+"&expenseReportCity="+expenseReport.getExpenseReportCity().replace(" ", "_")+"&expenseReportComment="+expenseReport.getExpenseReportComment().replace(" ", "_")+"&idUser="+expenseReport.getIdUser()+"&idCustomer="+idCustomerSelected;
                        //String myURL2 = "http://10.0.2.2/API/public/expensereport/add?expenseReportDate="+parseDateToUS(expenseReport.getExpenseReportDate())+"&expenseReportCity="+expenseReport.getExpenseReportCity().replace(" ", "_")+"&expenseReportComment="+expenseReport.getExpenseReportComment().replace(" ", "_")+"&idUser="+expenseReport.getIdUser()+"&idCustomer="+idCustomerSelected;
                        HttpGetRequest getRequest = new HttpGetRequest();
                        try {
                            result2 = getRequest.execute(myURL2).get();
                            expenseReport.setExpenseReportCode(Integer.parseInt(result2));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        //System.out.println("Retour HTTPGetRequest ExpenseReport : " + result2);


                        /* lien vers la vue ajouter une dépense */
                        Intent intent = new Intent(NewERActivity.this, NewExpenseActivity.class);
                        /* Je transmets à la vue suivante les données de la note de frais pour les relier aux dépenses */
                        startActivity(intent);

                        SharedPreferences sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, Context.MODE_PRIVATE);
                        sharedPreferencesER.edit()
                                .putString(EXPENSE_REPORT_KEY, expenseReport.toJSON())
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
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Lien vers la vue Notes de Frais */
                Intent intent = new Intent(NewERActivity.this, ExpenseReportActivity.class);
                startActivity(intent);
            }
        });
    }

    /** Gestion de la sélection sur la liste déroulante */
    private void addListenerOnSpinnerItemSelection() {
        clientsSpinner.setOnItemSelectedListener(this);
    }

    /** Gestion de l'item sélectionné dans la liste déroulante */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        customerSelected = (Customer) adapterView.getSelectedItem();
        idCustomerSelected = customerSelected.getIdCustomer();
        //System.out.println("Client selectionne: "+customerSelected.getIdCustomer());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Another interface callback
    }

    /** fonction qui parse un String en Date */
    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);
        return newDate;
    }

    /** fonction qui change la Date format FR au format US */
    public String parseDateToUS(String date) throws ParseException {
        SimpleDateFormat formatFR = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatUS = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFR = formatFR.parse(date);
        String dateUS = formatUS.format(dateFR);
        return dateUS;
    }
}

