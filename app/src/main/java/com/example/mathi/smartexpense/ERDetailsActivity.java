package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    final String EXPENSE_LABEL = "expense_label";
    final String EXPENSE_ID = "expense_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erdetails);

        final Intent intent = getIntent();

/* Gestion du clic sur le bouton Retour */
        Button returnButton = (Button) findViewById(R.id.returnButtonERDetails);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Notes de frais */
                Intent intent2 = new Intent(ERDetailsActivity.this, ExpenseReportActivity.class);
                startActivity(intent2);
            }
        });

/* Gestion du clic sur le bouton Ajouter une dépense */
        Button addButton = (Button) findViewById(R.id.addExpenseButton);
        /*if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            addButton.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.VISIBLE);
        }*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Nouvelle dépense */
                Intent intent3 = new Intent(ERDetailsActivity.this, NewExpenseActivity.class);
                startActivity(intent3);
            }
        });

/* Gestion du clic sur le bouton Sauver */
        Button saveButton = (Button) findViewById(R.id.saveButton);
        /*if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            saveButton.setVisibility(View.GONE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
        }*/
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Création de la note de frais et des dépenses dans la db */

            }
        });

/* Gestion du clic sur le bouton Soumettre */
        Button submitButton = (Button) findViewById(R.id.submitButton);
        /*if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            submitButton.setVisibility(View.GONE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
        }*/
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Mise à jour de la note de frais dans la db : création de la date de soumission */
                //String myURL = "http://www.gyejacquot-pierre.fr/API/public/expensereport/update?expenseReportCode="+intent.getStringExtra("expense_report_code");
                String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expensereport/update?expenseReportCode="+intent.getStringExtra("expense_report_code");
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    getRequest.execute(myURL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

/* Récupération des données de la page précédente et injection dans les TextView de la vue */
        TextView code = (TextView) findViewById(R.id.numberER);
        code.setText("Note de frais "+intent.getStringExtra("expense_report_code"));

        TextView date = (TextView) findViewById(R.id.ERDate);
        date.setText(intent.getStringExtra("expense_report_date"));

        TextView status = (TextView) findViewById(R.id.ERStatus);
        if (!intent.getStringExtra("expense_report_submission_date").equals("null")) {
            status.setText(intent.getStringExtra("expense_report_submission_date"));
        } else {
            status.setText("Non soumise");
        }

        TextView city = (TextView) findViewById(R.id.ERCity);
        city.setText(intent.getStringExtra("expense_report_city"));

/* Gestion de la ListView */
        liste = findViewById(R.id.listERDetails);
        List<Expense> eList = new ArrayList<Expense>();
        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses?expenseReportCode="+intent.getStringExtra("expense_report_code");
        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expenses?expenseReportCode="+intent.getStringExtra("expense_report_code");
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            String result = getRequest.execute(myURL).get();
            System.out.println("Retour HTTPGetRequest : " + result);
            JSONArray array = new JSONArray(result);
            for (int i= 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                String comment = "";
                if (obj.getString("expenseDetails").equals("null")) {
                    comment = "";
                } else {
                    comment = obj.getString("expenseDetails");
                }
                eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, obj.getInt("expenseTotal")));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ExpenseAdapter(ERDetailsActivity.this, eList);
        liste.setAdapter(adapter);

/* Gestion du clic sur une cellule de la ListView */
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                Intent intent4 = new Intent(ERDetailsActivity.this, ExpenseDetailsActivity.class);
                intent4.putExtra(EXPENSE_LABEL, adapter.getItem(position).getLabel());
                intent4.putExtra(EXPENSE_ID, adapter.getItem(position).getIdExpense());
                startActivity(intent4);
            }
        });

    }
}
