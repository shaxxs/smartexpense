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

import com.example.mathi.smartexpense.model.Travel;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class ExpenseMoreDetailsActivity extends AppCompatActivity {

    final String TRAVEL_KEY = "travel";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    SharedPreferences sharedPreferencesER;

    private Travel travel = new Travel();

    private ImageView image;
    TextView departureDate;
    TextView returnDate;
    TextView departureCity;
    TextView destinationCity;
    TextView duration;
    TextView km;
    private LinearLayout returnLinearLayout;
    private LinearLayout departLinearLayout;
    private LinearLayout returnCLinearLayout;
    private LinearLayout departCLinearLayout;
    private LinearLayout kmLinearLayout;
    private LinearLayout durationLinearLayout;
    private Button buttonReturn;
    private Button buttonProof;

    String jsonTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_more_details);

        buttonReturn = (Button) findViewById(R.id.returnButtonExpenseMoreDetails);
        buttonProof = (Button) findViewById(R.id.proofButton);
        departureDate = (TextView) findViewById(R.id.departureDate);
        returnDate = (TextView) findViewById(R.id.expenseReturnDate);
        departureCity = (TextView) findViewById(R.id.expenseDepartureCity);
        destinationCity = (TextView) findViewById(R.id.expenseDestinationCity);
        duration = (TextView) findViewById(R.id.expenseTravelDuration);
        km = (TextView) findViewById(R.id.expenseKm);
        image = findViewById(R.id.imageProof);
        returnLinearLayout = findViewById(R.id.returnLinearLayout);
        departLinearLayout = findViewById(R.id.departureLinearLayout);
        returnCLinearLayout = findViewById(R.id.departureCLinearLayout);
        departCLinearLayout = findViewById(R.id.destCLinearLayout);
        kmLinearLayout = findViewById(R.id.kmLinearLayout);
        durationLinearLayout = findViewById(R.id.durationLinearLayout);

        /** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(TRAVEL_KEY)) {
            jsonTravel = sharedPreferencesER.getString(TRAVEL_KEY, null);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonTravel);
                travel = travel.jsonToTravel(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /** Injection des données de la dépense dans les TextView de la vue */
        departureDate.setText(travel.getDepartureDate());
        returnDate.setText(travel.getReturnDate());
        departureCity.setText(travel.getDepartureCity());
        destinationCity.setText(travel.getDestinationCity());
        km.setText(travel.getKm() + "km");
        if (travel.getTravelDuration().equals("0")){
            duration.setText("");
        } else {
            duration.setText(travel.getTravelDuration()+"h");
        }
        if (travel.getIdProof() == null) {
            buttonProof.setVisibility(View.GONE);
        }

        /** Gestion du clic sur le bouton Justificatif */
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
                image.setRotation(90);
                // URL de l'API qui permet de récupérer les données (photo) d'un justificatif
                String myURL = "http://www.gyejacquot-pierre.fr/API/public/proof?idProof=" + travel.getIdProof();
                //String myURL = "http://10.0.2.2/API/public/proof?idProof="+travel.getIdProof();
                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    // résultat de la requete http
                    String result = getRequest.execute(myURL).get();
                    // si la requete a été correctement effectuée
                    if (!result.isEmpty()) {
                        byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
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

        /** Gestion du clic sur le bouton Retour */
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Note de frais */
                Intent intentReturn = new Intent(ExpenseMoreDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intentReturn);
            }
        });

    }
}
