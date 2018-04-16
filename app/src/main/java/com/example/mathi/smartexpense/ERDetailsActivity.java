package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.model.ExpenseAdapter;
import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.model.ExpenseReportAdapter;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ERDetailsActivity extends AppCompatActivity {

    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String EXPENSE_REPORT_DATE = "expense_report_date";
    final String EXPENSE_REPORT_CITY = "expense_report_city";
    final String EXPENSE_REPORT_SUBMISSION_DATE = "expense_report_submission_date";
    private ListView liste;
    ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erdetails);

    /* Gestion du clic sur le bouton Retour */
        Button returnButton = (Button) findViewById(R.id.returnButtonERDetails);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ERDetailsActivity.this, ExpenseReportActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

/* Gestion du clic sur le bouton Ajouter une dépense */
        Button addButton = (Button) findViewById(R.id.addExpenseButton);
        if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            addButton.setEnabled(false);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ERDetailsActivity.this, NewExpenseActivity.class);
                startActivity(intent2);
            }
        });

/* Gestion du clic sur le bouton Sauver */
        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            saveButton.setVisibility(View.GONE);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

/* Gestion du clic sur le bouton Soumettre */
        Button submitButton = (Button) findViewById(R.id.submitButton);
        if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            submitButton.setEnabled(false);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

/* Récupération des données de la page précédente */
        TextView code = (TextView) findViewById(R.id.numberER);
        code.setText("Note de frais "+intent.getStringExtra("expense_report_code"));

        TextView status = (TextView) findViewById(R.id.ERStatus);
        status.setText("Soumise le "+intent.getStringExtra("expense_report_submission_date"));

        TextView date = (TextView) findViewById(R.id.ERDate);
        if (!intent.getStringExtra("expense_report_date").equals("null")) {
            date.setText(intent.getStringExtra("expense_report_date"));
        } else {
            date.setText("Non soumise");
        }

        TextView city = (TextView) findViewById(R.id.ERCity);
        city.setText(intent.getStringExtra("expense_report_city"));

/* Gestion de la ListView */
        liste = findViewById(R.id.listERDetails);
        List<Expense> eList = new ArrayList<Expense>();
        String myURL = "http://www.mathildeperson.fr/se/erdetails.php?expenseReportCodeB="+intent.getStringExtra("expense_report_code");
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            String result = getRequest.execute(myURL).get();
            System.out.println("Retour HTTPGetRequest : " + result);
            JSONArray array = new JSONArray(result);
            for (int i= 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                String comment = "";
                if (obj.getString("businessExpenseDetails").equals("null")) {
                    comment = "";
                } else {
                    comment = obj.getString("businessExpenseDetails");
                }
                eList.add(new Expense(obj.getInt("idExpenseB"), obj.getString("businessExpenseDate"), obj.getString("businessExpenseLabel"), comment, obj.getInt("expenseTotalB")));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ExpenseAdapter(ERDetailsActivity.this, eList);
        liste.setAdapter(adapter);

    }
}
