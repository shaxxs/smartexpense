package com.example.mathi.smartexpense;

import android.content.Intent;
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
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        final Intent intent3 = getIntent();

/* Gestion du clic sur le bouton Retour */
        Button button1 = (Button) findViewById(R.id.returnButtonExpenseDetails);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Note de frais - Détails */
                Intent intent = new Intent(ExpenseDetailsActivity.this, ERDetailsActivity.class);
                startActivity(intent);
            }
        });

/* Gestion du clic sur le bouton Détails */
        button2 = (Button) findViewById(R.id.detailsButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Dépense - Détails */
                Intent intent2 = new Intent(ExpenseDetailsActivity.this, ExpenseMoreDetailsActivity.class);
                intent2.putExtra(EXPENSE_ID, intent3.getIntExtra("expense_id", 0));
                startActivity(intent2);
            }
        });

/* Déclaration des TextView de la vue */
        TextView label = (TextView) findViewById(R.id.expenseCategory);
        label.setText(intent3.getStringExtra("expense_label"));
        TextView date = (TextView) findViewById(R.id.expenseDate);
        TextView status = (TextView) findViewById(R.id.expenseStatus);
        TextView validationDate = (TextView) findViewById(R.id.expenseValidationDate);
        TextView totalAmount = (TextView) findViewById(R.id.expenseTotalAmount);
        TextView refundAmount = (TextView) findViewById(R.id.expenseRefundAmount);
        TextView paymentDate = (TextView) findViewById(R.id.expensePaymentDate);

/* Récupération des données d'une dépense et injection dans les TextView de la vue */
        /* Si c'est un trajet */
        if (intent3.getStringExtra("expense_label").equals("Trajet")) {
            //String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel?idExpenseT="+Integer.parseInt(intent3.getStringExtra("expense_id"));
            String myURL = "http:/10.0.2.2/smartExpenseApi/API/public/travel?idExpenseT=" + intent3.getIntExtra("expense_id", 0);
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
            button2.setVisibility(View.GONE);
            //String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense?idExpenseB="+Integer.parseInt(intent3.getStringExtra("expense_id"));
            String myURL = "http://10.0.2.2/smartExpenseApi/API/public/businessexpense?idExpenseB="+intent3.getIntExtra("expense_id", 0);
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
