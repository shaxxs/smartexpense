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

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ExpenseDetailsActivity extends AppCompatActivity {

    final String EXPENSE_LABEL = "expense_label";
    final String EXPENSE_ID = "expense_id";
    final String REFUND_TRACKER = "refund_tracker";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    private Button buttonDetails;
    private Button buttonProof;
    private Button buttonReturn;
    SharedPreferences sharedPreferencesER;
    private String expLabel;
    private int expId;
    private Boolean refundTracker; // (pour le bouton Retour) indique si on vient de la page Suivi des remboursements ou Note de frais Détails, true pour Suivi des rb, false pour Note de frais
    private LinearLayout dateLayout;
    private LinearLayout statusLayout;
    private LinearLayout dateValidLayout;
    private LinearLayout amountLayout;
    private LinearLayout amountRefLayout;
    private LinearLayout paymLayout;
    private ImageView image;
    private int idProof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

/** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_LABEL) && sharedPreferencesER.contains(EXPENSE_ID) && sharedPreferencesER.contains(REFUND_TRACKER)) {
            expLabel = sharedPreferencesER.getString(EXPENSE_LABEL, null);
            expId = sharedPreferencesER.getInt(EXPENSE_ID, 0);
            refundTracker = sharedPreferencesER.getBoolean(REFUND_TRACKER, false);
        }

/** Gestion du clic sur le bouton Retour */
        buttonReturn = (Button) findViewById(R.id.returnButtonExpenseDetails);
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
        buttonDetails = (Button) findViewById(R.id.detailsButton);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Dépense - Détails */
                Intent intentDetails = new Intent(ExpenseDetailsActivity.this, ExpenseMoreDetailsActivity.class);
                startActivity(intentDetails);
            }
        });

/** Gestion du clic sur le bouton Justificatif */
        image = findViewById(R.id.imageProof);
        dateLayout = findViewById(R.id.dateLayout);
        dateValidLayout = findViewById(R.id.dateValidLayout);
        statusLayout = findViewById(R.id.statusLayout);
        amountLayout = findViewById(R.id.amountLayout);
        amountRefLayout = findViewById(R.id.amountRefLayout);
        paymLayout = findViewById(R.id.paymLayout);
        buttonProof = (Button) findViewById(R.id.proofButton);
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


/** Déclaration des TextView de la vue */
        TextView label = (TextView) findViewById(R.id.expenseCategory);
        label.setText(expLabel);
        TextView date = (TextView) findViewById(R.id.expenseDate);
        TextView status = (TextView) findViewById(R.id.expenseStatus);
        TextView validationDate = (TextView) findViewById(R.id.expenseValidationDate);
        TextView totalAmount = (TextView) findViewById(R.id.expenseTotalAmount);
        TextView refundAmount = (TextView) findViewById(R.id.expenseRefundAmount);
        TextView paymentDate = (TextView) findViewById(R.id.expensePaymentDate);

/** Récupération des données d'une dépense et injection dans les TextView de la vue */
        /** Si c'est un trajet */
        if (expLabel.equals("Trajet")) {
            buttonDetails.setVisibility(View.VISIBLE);
            buttonProof.setVisibility(View.GONE);
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
                    date.setText(obj.getString("departureDate"));
                    totalAmount.setText(String.valueOf(obj.getInt("expenseTotalT")) + "€");
                    // si le champ validationState est null
                    if (obj.isNull("validationState")) {
                        status.setText("Non soumise");
                    } else {
                        status.setText(obj.getString("validationState"));
                    }
                    // si le champ dateValidation est null
                    if (obj.isNull("dateValidation")) {
                        validationDate.setText("");
                    } else {
                        validationDate.setText(obj.getString("dateValidation"));
                    }
                    // si le champ paymentDateT est null
                    if (obj.isNull("paymentDateT")) {
                        paymentDate.setText("");
                    } else {
                        paymentDate.setText(obj.getString("paymentDateT"));
                    }
                    // si le champ refundAmountT est null
                    if (obj.isNull("refundAmountT")) {
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(String.valueOf(obj.getInt("refundAmountT")) + "€");
                    }
                // si le résultat est vide, l'appli ne crash pas, les champs sont vides
                } else {
                    date.setText("");
                    totalAmount.setText("");
                    status.setText("");
                    paymentDate.setText("");
                    refundAmount.setText("");
                    validationDate.setText("");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        /** Si c'est un autre frais */
        } else {
            /* Disparition du bouton Détails (qui mène vers les infos d'un trajet) */
            buttonDetails.setVisibility(View.GONE);
            buttonProof.setVisibility(View.VISIBLE);
            // URL de l'API qui permet de récupérer les données d'une dépense
            String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense?idExpenseB="+expId;
            //String myURL = "http://10.0.2.2/API/public/businessexpense?idExpenseB="+expId;

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
                    date.setText(obj.getString("businessExpenseDate"));
                    totalAmount.setText(String.valueOf(obj.getInt("expenseTotalB")) + "€");
                    // si le champ validationState est null
                    if (obj.isNull("validationState")) {
                        status.setText("Non soumise");
                    } else {
                        status.setText(obj.getString("validationState"));
                    }
                    // si le champ dateValidation est null
                    if (obj.isNull("dateValidation")) {
                        validationDate.setText("");
                    } else {
                        validationDate.setText(String.valueOf(setDateFormat(obj.getString("dateValidation"))));
                    }
                    // si le champ paymentDateB est null
                    if (obj.isNull("paymentDateB")) {
                        paymentDate.setText("");
                    } else {
                        paymentDate.setText(String.valueOf(setDateFormat(obj.getString("paymentDateB"))));
                    }
                    // si le champ refundAmountB est null
                    if (obj.isNull("refundAmountB")) {
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(String.valueOf(obj.getInt("refundAmountB")) + "€");
                    }
                    idProof = obj.getInt("idProofB");
                // si le résultat est vide, l'appli ne crash pas, les champs sont vides
                } else {
                    date.setText("");
                    totalAmount.setText("");
                    status.setText("");
                    paymentDate.setText("");
                    refundAmount.setText("");
                    validationDate.setText("");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
