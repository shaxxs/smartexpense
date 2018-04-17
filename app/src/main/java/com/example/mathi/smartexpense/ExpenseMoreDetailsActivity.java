package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ExpenseMoreDetailsActivity extends AppCompatActivity {

    final String EXPENSE_ID = "expense_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_more_details);

        final Intent intent1 = getIntent();

/* Gestion du clic sur le bouton Retour */
        Button button1 = (Button) findViewById(R.id.returnButtonExpenseMoreDetails);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Dépense - Détails */
                Intent intent = new Intent(ExpenseMoreDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intent);
            }
        });

/* Gestion du clic sur le bouton Justificatif */
        Button button2 = (Button) findViewById(R.id.proofButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

/* Déclaration des TextView de la vue */
        TextView departureDate = (TextView) findViewById(R.id.departureDate);
        TextView returnDate = (TextView) findViewById(R.id.expenseReturnDate);
        TextView departureCity = (TextView) findViewById(R.id.expenseDepartureCity);
        TextView destinationCity = (TextView) findViewById(R.id.expenseDestinationCity);
        TextView duration = (TextView) findViewById(R.id.expenseTravelDuration);
        TextView km = (TextView) findViewById(R.id.expenseKm);

/* Récupération des données d'une dépense et injection dans les TextView de la vue */
            //String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+Integer.parseInt(intent3.getStringExtra("expense_id"));
            String myURL = "http:/10.0.2.2/smartExpenseApi/API/public/travel?idExpenseT=" + intent1.getIntExtra("expense_id", 0);
            HttpGetRequest getRequest = new HttpGetRequest();
            try {
                String result = getRequest.execute(myURL).get();
                System.out.println("Retour HTTPGetRequest : " + result);
                if (!result.isEmpty()) {
                    JSONArray array = new JSONArray(result);
                    JSONObject obj = new JSONObject(array.getString(0));
                    departureDate.setText(obj.getString("departureDate"));
                    returnDate.setText(obj.getString("returnDate"));
                    departureCity.setText(obj.getString("departureCity"));
                    destinationCity.setText(obj.getString("destinationCity"));
                    km.setText(String.valueOf(obj.getInt("km")) + "km");
                    if (obj.getString("travelDuration").equals("null")) {
                        duration.setText("");
                    } else {
                        duration.setText(obj.getString("travelDuration"));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
