package com.example.mathi.smartexpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ExpenseMoreDetailsActivity extends AppCompatActivity {

    final String EXPENSE_ID = "expense_id";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    SharedPreferences sharedPreferencesER;
    private int expId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_more_details);

/** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_ID)) {
            expId = sharedPreferencesER.getInt(EXPENSE_ID, 0);
        }

/** Gestion du clic sur le bouton Retour */
        Button buttonReturn = (Button) findViewById(R.id.returnButtonExpenseMoreDetails);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Note de frais */
                Intent intentReturn = new Intent(ExpenseMoreDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intentReturn);
            }
        });

/** Gestion du clic sur le bouton Justificatif */
        Button buttonProof = (Button) findViewById(R.id.proofButton);
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

/** Déclaration des TextView de la vue */
        TextView departureDate = (TextView) findViewById(R.id.departureDate);
        TextView returnDate = (TextView) findViewById(R.id.expenseReturnDate);
        TextView departureCity = (TextView) findViewById(R.id.expenseDepartureCity);
        TextView destinationCity = (TextView) findViewById(R.id.expenseDestinationCity);
        TextView duration = (TextView) findViewById(R.id.expenseTravelDuration);
        TextView km = (TextView) findViewById(R.id.expenseKm);

/** Récupération des données d'une dépense et injection dans les TextView de la vue */
        // URL de l'API qui permet de récupérer les données d'une dépense
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+expId;
        //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/travel?idExpenseT="+expId;
        // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            // résultat de la requete http
            String result = getRequest.execute(myURL).get();
            // si la requete a été correctement effectuée
            if (!result.isEmpty()) {
                // tableau JSON qui contient le résultat
                JSONArray array = new JSONArray(result);
                // objet JSON qui contient les données de la dépense (pas de boucle sur le tableau, il n'y a qu'un objet, 1 dépense = 1 objet)
                JSONObject obj = new JSONObject(array.getString(0));
                // on injecte les données de la dépense dans les TextView
                departureDate.setText(String.valueOf(setDateFormat(obj.getString("departureDate"))));
                returnDate.setText(String.valueOf(setDateFormat(obj.getString("returnDate"))));
                departureCity.setText(obj.getString("departureCity"));
                destinationCity.setText(obj.getString("destinationCity"));
                km.setText(String.valueOf(obj.getInt("km")) + "km");
                // si le champ travelDuration est null
                if (obj.isNull("travelDuration")) {
                    duration.setText("");
                } else {
                    duration.setText(obj.getString("travelDuration"));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

/** fonction qui transforme la date format US au format FR */
    public String setDateFormat(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);

        format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(newDate);
        return date;
    }

}
