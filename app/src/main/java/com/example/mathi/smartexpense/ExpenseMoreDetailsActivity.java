package com.example.mathi.smartexpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ImageView image;
    private TextView departureDate;
    private TextView returnDate;
    private TextView departureCity;
    private TextView destinationCity;
    private TextView duration;
    private TextView km;
    private LinearLayout returnLinearLayout;
    private LinearLayout departLinearLayout;
    private LinearLayout returnCLinearLayout;
    private LinearLayout departCLinearLayout;
    private LinearLayout kmLinearLayout;
    private LinearLayout durationLinearLayout;
    private Button buttonReturn;
    private Button buttonProof;
    private int idProof;

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
        buttonReturn = (Button) findViewById(R.id.returnButtonExpenseMoreDetails);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Note de frais */
                Intent intentReturn = new Intent(ExpenseMoreDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intentReturn);
            }
        });

/** Gestion du clic sur le bouton Justificatif */
        image = findViewById(R.id.imageProof);
        returnLinearLayout = findViewById(R.id.returnLinearLayout);
        departLinearLayout = findViewById(R.id.departureLinearLayout);
        returnCLinearLayout = findViewById(R.id.departureCLinearLayout);
        departCLinearLayout = findViewById(R.id.destCLinearLayout);
        kmLinearLayout = findViewById(R.id.kmLinearLayout);
        durationLinearLayout = findViewById(R.id.durationLinearLayout);
        buttonProof = (Button) findViewById(R.id.proofButton);
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnLinearLayout.setVisibility(View.GONE);
                departLinearLayout.setVisibility(View.GONE);
                returnCLinearLayout.setVisibility(View.GONE);
                kmLinearLayout.setVisibility(View.GONE);
                departCLinearLayout.setVisibility(View.GONE);
                durationLinearLayout.setVisibility(View.GONE);
                buttonReturn.setVisibility(View.GONE);
                buttonProof.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                // URL de l'API qui permet de récupérer les données d'un justificatif
                String myURL2 = "http://www.gyejacquot-pierre.fr/API/public/proof?idProof="+idProof;
                //String myURL2 = "http://10.0.2.2/API/public/proof?idProof="+idProof;
                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    // résultat de la requete http
                    String result2 = getRequest.execute(myURL2).get();
                    // si la requete a été correctement effectuée
                    if (!result2.isEmpty()) {
                        byte[] decodedString = Base64.decode(result2, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image.setImageBitmap(decodedByte);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

/** Gestion du clic sur la photo */
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.GONE);
                returnLinearLayout.setVisibility(View.VISIBLE);
                departLinearLayout.setVisibility(View.VISIBLE);
                returnCLinearLayout.setVisibility(View.VISIBLE);
                kmLinearLayout.setVisibility(View.VISIBLE);
                departCLinearLayout.setVisibility(View.VISIBLE);
                durationLinearLayout.setVisibility(View.VISIBLE);
                buttonReturn.setVisibility(View.VISIBLE);
                buttonProof.setVisibility(View.VISIBLE);
            }
        });

/** Déclaration des TextView de la vue */
        departureDate = (TextView) findViewById(R.id.departureDate);
        returnDate = (TextView) findViewById(R.id.expenseReturnDate);
        departureCity = (TextView) findViewById(R.id.expenseDepartureCity);
        destinationCity = (TextView) findViewById(R.id.expenseDestinationCity);
        duration = (TextView) findViewById(R.id.expenseTravelDuration);
        km = (TextView) findViewById(R.id.expenseKm);

/** Récupération des données d'une dépense et injection dans les TextView de la vue */
        // URL de l'API qui permet de récupérer les données d'une dépense
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+expId;
        //String myURL = "http://10.0.2.2/API/public/travel?idExpenseT="+expId;
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
                idProof = obj.getInt("idProofT");
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
