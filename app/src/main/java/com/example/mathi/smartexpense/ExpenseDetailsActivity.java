package com.example.mathi.smartexpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class ExpenseDetailsActivity extends AppCompatActivity {

    final String EXPENSE_LABEL = "expense_label";
    final String EXPENSE_ID = "expense_id";
    final String REFUND_TRACKER = "refund_tracker";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    private Button buttonDetails;
    SharedPreferences sharedPreferencesER;
    private String expLabel;
    private int expId;
    private Boolean refundTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

// on récupère les données de notre fichier SharedPreferences
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_LABEL) && sharedPreferencesER.contains(EXPENSE_ID) && sharedPreferencesER.contains(REFUND_TRACKER)) {
            expLabel = sharedPreferencesER.getString(EXPENSE_LABEL, null);
            expId = sharedPreferencesER.getInt(EXPENSE_ID, 0);
            refundTracker = sharedPreferencesER.getBoolean(REFUND_TRACKER, false);
        }

/* Gestion du clic sur le bouton Retour */
        Button buttonReturn = (Button) findViewById(R.id.returnButtonExpenseDetails);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (refundTracker.equals(false)) {
                /* Lien vers la vue Note de frais */
                Intent intentReturn = new Intent(ExpenseDetailsActivity.this, ERDetailsActivity.class);
                startActivity(intentReturn);
            } else {
                /* Lien vers la vue Suivi des remboursements */
                Intent intentReturn = new Intent(ExpenseDetailsActivity.this, RefundTrackerActivity.class);
                startActivity(intentReturn);
            }
            }
        });

/* Gestion du clic sur le bouton Détails */
        buttonDetails = (Button) findViewById(R.id.detailsButton);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Dépense - Détails */
                Intent intentDetails = new Intent(ExpenseDetailsActivity.this, ExpenseMoreDetailsActivity.class);
                startActivity(intentDetails);
            }
        });

/* Déclaration des TextView de la vue */
        TextView label = (TextView) findViewById(R.id.expenseCategory);
        label.setText(expLabel);
        TextView date = (TextView) findViewById(R.id.expenseDate);
        TextView status = (TextView) findViewById(R.id.expenseStatus);
        TextView validationDate = (TextView) findViewById(R.id.expenseValidationDate);
        TextView totalAmount = (TextView) findViewById(R.id.expenseTotalAmount);
        TextView refundAmount = (TextView) findViewById(R.id.expenseRefundAmount);
        TextView paymentDate = (TextView) findViewById(R.id.expensePaymentDate);

/* Récupération des données d'une dépense et injection dans les TextView de la vue */
        /* Si c'est un trajet */
        if (expLabel.equals("Trajet")) {
            //String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+Integer.parseInt(intent3.getStringExtra("expense_id"));
            String myURL = "http:/10.0.2.2/smartExpenseApi/API/public/travel?idExpenseT=" + expId;
            HttpGetRequest getRequest = new HttpGetRequest();
            try {
                String result = getRequest.execute(myURL).get();
                System.out.println("Retour HTTPGetRequest : " + result);
                if (!result.isEmpty()) {
                    JSONArray array = new JSONArray(result);
                    JSONObject obj = new JSONObject(array.getString(0));
                    date.setText(obj.getString("departureDate"));
                    totalAmount.setText(String.valueOf(obj.getInt("expenseTotalT")) + "€");
                    status.setText(obj.getString("validationState"));
                    if (obj.getString("dateValidation").equals("null")) {
                        validationDate.setText("");
                    } else {
                        validationDate.setText(obj.getString("dateValidation"));
                    }
                    if (obj.getString("paymentDateT").equals("null") || String.valueOf(obj.getInt("refundAmountT")).equals("null")) {
                        paymentDate.setText("");
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(String.valueOf(obj.getInt("refundAmountT")) + "€");
                        paymentDate.setText(obj.getString("paymentDateT"));
                    }
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
        /* Si c'est un autre frais */
        } else {
            /* Disparition du bouton Détails (qui mène vers les infos d'un trajet) */
            buttonDetails.setVisibility(View.GONE);
            //String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense?idExpenseB="+Integer.parseInt(intent3.getStringExtra("expense_id"));
            String myURL = "http://10.0.2.2/smartExpenseApi/API/public/businessexpense?idExpenseB="+expId;
            HttpGetRequest getRequest = new HttpGetRequest();
            try {
                String result = getRequest.execute(myURL).get();
                System.out.println("Retour HTTPGetRequest : " + result);
                if (!result.isEmpty()) {
                    JSONArray array = new JSONArray(result);
                    JSONObject obj = new JSONObject(array.getString(0));
                    date.setText(obj.getString("businessExpenseDate"));
                    totalAmount.setText(String.valueOf(obj.getInt("expenseTotalB")) + "€");
                    status.setText(obj.getString("validationState"));
                    if (obj.getString("dateValidation").equals("null")) {
                        validationDate.setText("");
                    } else {
                        validationDate.setText(obj.getString("dateValidation"));
                    }
                    if (obj.getString("paymentDateB").equals("null") || String.valueOf(obj.getInt("refundAmountB")).equals("null")) {
                        paymentDate.setText("");
                        refundAmount.setText("");
                    } else {
                        refundAmount.setText(String.valueOf(obj.getInt("refundAmountB")) + "€");
                        paymentDate.setText(obj.getString("paymentDateB"));
                    }
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
        }
    }
}
