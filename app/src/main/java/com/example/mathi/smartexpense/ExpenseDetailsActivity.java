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

import com.example.mathi.smartexpense.model.BusinessExpense;
import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.model.Travel;
import com.example.mathi.smartexpense.model.Valid;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class ExpenseDetailsActivity extends AppCompatActivity {

    final String EXPENSE_KEY = "expense";
    final String TRAVEL_KEY = "travel";
    final String REFUND_TRACKER = "refund_tracker";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    private Boolean refundTracker; // (pour le bouton Retour) indique si on vient de la page Suivi des remboursements ou Note de frais Détails, true pour Suivi des rb, false pour Note de frais
    private SharedPreferences sharedPreferencesER;

    private Expense expense = new Expense();
    private Valid valid = new Valid();
    private Travel travel = new Travel();
    private BusinessExpense businessExp = new BusinessExpense();

    Button buttonDetails;
    private Button buttonProof;
    private Button buttonReturn;
    TextView label;
    TextView date;
    TextView status;
    TextView validationDate;
    TextView totalAmount;
    TextView refundAmount;
    TextView paymentDate;
    private LinearLayout dateLayout;
    private LinearLayout statusLayout;
    private LinearLayout dateValidLayout;
    private LinearLayout amountLayout;
    private LinearLayout amountRefLayout;
    private LinearLayout paymLayout;
    private ImageView image;

    String jsonExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        buttonReturn = (Button) findViewById(R.id.returnButtonExpenseDetails);
        buttonDetails = (Button) findViewById(R.id.detailsButton);
        buttonProof = (Button) findViewById(R.id.proofButton);
        label = (TextView) findViewById(R.id.expenseCategory);
        date = (TextView) findViewById(R.id.expenseDate);
        status = (TextView) findViewById(R.id.expenseStatus);
        validationDate = (TextView) findViewById(R.id.expenseValidationDate);
        totalAmount = (TextView) findViewById(R.id.expenseTotalAmount);
        refundAmount = (TextView) findViewById(R.id.expenseRefundAmount);
        paymentDate = (TextView) findViewById(R.id.expensePaymentDate);
        image = findViewById(R.id.imageProof);
        dateLayout = findViewById(R.id.dateLayout);
        dateValidLayout = findViewById(R.id.dateValidLayout);
        statusLayout = findViewById(R.id.statusLayout);
        amountLayout = findViewById(R.id.amountLayout);
        amountRefLayout = findViewById(R.id.amountRefLayout);
        paymLayout = findViewById(R.id.paymLayout);

        /** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_KEY) && sharedPreferencesER.contains(REFUND_TRACKER)) {
            refundTracker = sharedPreferencesER.getBoolean(REFUND_TRACKER, false);
            jsonExpense = sharedPreferencesER.getString(EXPENSE_KEY, null);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonExpense);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            expense = expense.jsonToExpense(json);
        }

        /** Récupération des données d'une dépense et injection dans les TextView de la vue */
        label.setText(expense.getLabel());
        /** Si c'est un trajet */
        if (expense.getLabel().equals("Trajet")) {
            buttonDetails.setVisibility(View.VISIBLE);
            buttonProof.setVisibility(View.GONE);
            // URL de l'API qui permet de récupérer les données d'une dépense
            String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+expense.getIdExpense();
            //String myURL = "http://10.0.2.2/API/public/travel?idExpenseT="+expense.getIdExpense();

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
                    travel = travel.jsonToTravel(obj);
                    valid = valid.jsonToValid(obj);

                    // on injecte les données de la dépense dans les TextView
                    date.setText(travel.getDepartureDate());
                    totalAmount.setText(travel.getExpenseTotal() + "€");
                    paymentDate.setText(travel.getPaymentDate());
                    if (travel.getRefundAmount() == null){
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(travel.getRefundAmount() + "€");
                    }
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        /** Si c'est un autre frais */
        } else {
            /* Disparition du bouton Détails (qui mène vers les infos d'un trajet) */
            buttonDetails.setVisibility(View.GONE);
            buttonProof.setVisibility(View.VISIBLE);
            // URL de l'API qui permet de récupérer les données d'une dépense
            String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense?idExpenseB="+expense.getIdExpense();
            //String myURL = "http://10.0.2.2/API/public/businessexpense?idExpenseB="+expense.getIdExpense();

            // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
            HttpGetRequest getRequest = new HttpGetRequest();
            try {
                // résultat de la requete http
                String result = getRequest.execute(myURL).get();
                //System.out.println("HTTP RESULT : "+result);
                // si la requete a été correctement effectuée
                if (!result.isEmpty()) {
                    // tableau JSON qui contient le résultat
                    JSONArray array = new JSONArray(result);
                    // objet JSON qui contient les données de la dépense (pas de boucle sur le tableau, il n'y a qu'un objet, 1 dépense = 1 objet)
                    JSONObject obj = new JSONObject(array.getString(0));
                    businessExp = businessExp.jsonToBusinessExpense(obj);
                    valid = valid.jsonToValid(obj);

                    // on injecte les données de la dépense dans les TextView
                    date.setText(businessExp.getBusinessExpenseDate());
                    totalAmount.setText(businessExp.getExpenseTotal() + "€");
                    paymentDate.setText(businessExp.getPaymentDate());
                    if (businessExp.getRefundAmount() == null){
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(businessExp.getRefundAmount() + "€");
                    }
                    if (businessExp.getIdProof() == null){
                        buttonProof.setVisibility(View.GONE);
                    }
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }
        // on injecte les données de la dépense dans les TextView
        status.setText(valid.getValidationState());
        validationDate.setText(valid.getValidationDate());

        /** Gestion du clic sur le bouton Justificatif */
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateValidLayout.setVisibility(View.GONE);
                statusLayout.setVisibility(View.GONE);
                amountLayout.setVisibility(View.GONE);
                amountRefLayout.setVisibility(View.GONE);
                paymLayout.setVisibility(View.GONE);
                dateLayout.setVisibility(View.GONE);
                buttonReturn.setVisibility(View.GONE);
                buttonProof.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                image.setRotation(90);
                // URL de l'API qui permet de récupérer les données (photo) d'un justificatif
                String myURL2 = "http://www.gyejacquot-pierre.fr/API/public/proof?idProof="+businessExp.getIdProof();
                //String myURL2 = "http://10.0.2.2/API/public/proof?idProof="+businessExp.getIdProof();
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

        /** Gestion du clic sur la photo du justificatif */
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.GONE);
                dateValidLayout.setVisibility(View.VISIBLE);
                statusLayout.setVisibility(View.VISIBLE);
                amountLayout.setVisibility(View.VISIBLE);
                amountRefLayout.setVisibility(View.VISIBLE);
                paymLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.VISIBLE);
                buttonReturn.setVisibility(View.VISIBLE);
                buttonProof.setVisibility(View.VISIBLE);
            }
        });

        /** Gestion du clic sur le bouton Retour */
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // si on vient de la page Note de frais - détails
                if (refundTracker.equals(false)) {
                /* Lien vers la vue Note de frais - détails */
                    Intent intentReturn = new Intent(ExpenseDetailsActivity.this, ERDetailsActivity.class);
                    startActivity(intentReturn);
                    // si on vient de la page Suivi des remboursements
                } else {
                /* Lien vers la vue Suivi des remboursements */
                    Intent intentReturn = new Intent(ExpenseDetailsActivity.this, RefundTrackerActivity.class);
                    startActivity(intentReturn);
                }
            }
        });

        /** Gestion du clic sur le bouton Détails */
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on ajoute les données de la cellule (dépense) cliquée au fichier SharedPreferences
                sharedPreferencesER.edit()
                        .putString(TRAVEL_KEY, travel.toJSON())
                        .putBoolean(REFUND_TRACKER, false)
                        .apply();
                /* Lien vers la vue Dépense - Détails */
                Intent intentDetails = new Intent(ExpenseDetailsActivity.this, ExpenseMoreDetailsActivity.class);
                startActivity(intentDetails);
            }
        });
    }
}
