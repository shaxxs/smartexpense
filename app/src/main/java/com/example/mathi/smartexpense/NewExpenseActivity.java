package com.example.mathi.smartexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class NewExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner categorySpinner;
    private EditText departureCity;
    private EditText dateExpense;
    private EditText dateDeparture;
    private EditText dateArrival;
    private EditText arrivalCity;
    private EditText kms;
    private EditText amount;
    private EditText details;
    private EditText durationTravel;
    private String category;
    private String ExpenseReportCodeTemporaire = "FAA011";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newexpense);

        dateExpense = (EditText) findViewById(R.id.dateExpense);
        dateDeparture = (EditText) findViewById(R.id.dateDeparture);
        dateArrival = (EditText) findViewById(R.id.dateArrival);
        departureCity = (EditText) findViewById(R.id.DepartureCity);
        arrivalCity = (EditText) findViewById(R.id.ArrivalCity);
        kms = (EditText) findViewById(R.id.kms);
        amount = (EditText) findViewById(R.id.amount);
        details = (EditText) findViewById(R.id.details);
        durationTravel = (EditText) findViewById(R.id.durationTravel);

//Gestion du clic sur le bouton Valider
        final Button buttonValid = (Button) findViewById(R.id.validButton);
        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Enregistrement de la nouvelle dépense dans la db
                //Si c'est un trajet
                if (category.equals("Trajet")) {
                    //String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity.getText()+"&destinationCity="+arrivalCity.getText()+"&departureDate="+dateDeparture.getText()+"&returnDate="+dateArrival.getText()+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+ExpenseReportCodeTemporaire;
                    String myURL = "http://10.0.2.2/smartExpenseApi/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity.getText()+"&destinationCity="+arrivalCity.getText()+"&departureDate="+dateDeparture.getText()+"&returnDate="+dateArrival.getText()+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+ExpenseReportCodeTemporaire;
                    HttpGetRequest getRequest = new HttpGetRequest();
                    String result = "";
                    try {
                        result = getRequest.execute(myURL).get();
                        System.out.println("Retour HTTPGetRequest : " + result);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("Succes")) {
                        Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée",Toast.LENGTH_SHORT).show();
                    }

                //Si c'est un businessexpense
                } else {
                    //String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense/create?expenseTotalB="+String.valueOf(amount.getText())+"&businessExpenseDetails="+details.getText()+"&businessExpenseDate="+dateExpense.getText()+"&expenseReportCodeB="+ExpenseReportCodeTemporaire;
                    String myURL = "http://10.0.2.2/smartExpenseApi/API/public/businessexpense/create?expenseTotalB="+String.valueOf(amount.getText())+"&businessExpenseLabel="+category+"&businessExpenseDetails="+details.getText()+"&businessExpenseDate="+dateExpense.getText()+"&expenseReportCodeB="+ExpenseReportCodeTemporaire;
                    HttpGetRequest getRequest = new HttpGetRequest();
                    String result = "";
                    try {
                        result = getRequest.execute(myURL).get();
                        System.out.println("Retour HTTPGetRequest : " + result);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("Succes")) {
                        Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée",Toast.LENGTH_SHORT).show();
                    }
                }
                //Lien vers la vue Note de frais
                Intent intentValid = new Intent(NewExpenseActivity.this, ExpenseReportActivity.class);
                startActivity(intentValid);
            }
        });

//Gestion du clic sur le bouton Ajouter un justificatif
        Button buttonProof = (Button) findViewById(R.id.btnAddProof);
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

//Gestion du clic sur le bouton Retour
        Button buttonReturn = (Button) findViewById(R.id.returnButton);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lien vers la vue Note de Frais
                Intent intentReturn = new Intent(NewExpenseActivity.this, ExpenseReportActivity.class);
                startActivity(intentReturn);
            }
        });

//Gestion de la liste déroulante
        addListenerOnSpinnerItemSelection();
    }

//Gestion de la liste déroulante
    public void addListenerOnSpinnerItemSelection() {
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(this);
    }

//Gestion de l'item sélectionné dans la liste déroulante et des champs affichés sur la vue
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        category = parent.getItemAtPosition(pos).toString();
        if (category.equals("Trajet")) {
            departureCity.setVisibility(View.VISIBLE);
            dateExpense.setVisibility(View.GONE);
            dateDeparture.setVisibility(View.VISIBLE);
            dateArrival.setVisibility(View.VISIBLE);
            arrivalCity.setVisibility(View.VISIBLE);
            kms.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
            durationTravel.setVisibility(View.VISIBLE);
            departureCity.setVisibility(View.VISIBLE);
        } else {
            departureCity.setVisibility(View.GONE);
            dateExpense.setVisibility(View.VISIBLE);
            dateDeparture.setVisibility(View.GONE);
            dateArrival.setVisibility(View.GONE);
            arrivalCity.setVisibility(View.GONE);
            kms.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
            durationTravel.setVisibility(View.GONE);
            departureCity.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
