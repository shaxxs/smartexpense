package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.model.ExpenseReportAdapter;
import com.example.mathi.smartexpense.model.ListViewExpenseReport;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseReportActivity extends AppCompatActivity {

    private ListView liste;
    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String EXPENSE_REPORT_DATE = "expense_report_date";
    final String EXPENSE_REPORT_CITY = "expense_report_city";
    final String EXPENSE_REPORT_SUBMISSION_DATE = "expense_report_submission_date";
    ExpenseReportAdapter adapter;
    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        Intent intent = getIntent();
        /* récupération des données de la vue précédente */
        SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        JSONObject userProfile = null;
        try {
            userProfile = new JSONObject(user_profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (userProfile != null) {
                Log.v("Data SharedPreferences", userProfile.getString("email") + "/" + userProfile.getString("nom"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Gestion du clic sur le bouton Ajouter une nouvelle note de frais
        Button boutonAdd = findViewById(R.id.addERButton);
        boutonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lien vers la vue Ajouter une Note de Frais
                Intent intentNewER = new Intent(ExpenseReportActivity.this, NewERActivity.class);
                startActivity(intentNewER);
            }
        });

/* Gestion du clic sur le bouton retour */
        Button boutonReturn = findViewById(R.id.returnButton);
        boutonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intent = new Intent(ExpenseReportActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

/* Gestion du clic sur le bouton ajouter une note */
        Button bouton2 = findViewById(R.id.addERButton);
        bouton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Nouvelle note de frais */
                Intent intent2 = new Intent(ExpenseReportActivity.this, NewERActivity.class);
                startActivity(intent2);
            }
        });

/* Gestion de la ListView */
        String idUser = null;
        try {
            idUser = userProfile.getString("idUser");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        liste = findViewById(R.id.listExpenseReport);
        List<ExpenseReport> erList = new ArrayList<ExpenseReport>();
        String myURL = "http://www.mathildeperson.fr/se/er.php?idUser="+idUser;
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            String result = getRequest.execute(myURL).get();
            System.out.println("Retour HTTPGetRequest : " + result);
            JSONArray array = new JSONArray(result);
            for (int i= 0; i < 6; i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                String comment = "";
                if (obj.getString("expenseReportComment").equals("null")) {
                    comment = "";
                } else {
                    comment = obj.getString("expenseReportComment");
                }
                erList.add(new ExpenseReport(obj.getString("expenseReportDate"), obj.getString("expenseReportCity"),comment, obj.getString("expenseReportCode"), obj.getString("submissionDate")));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ExpenseReportAdapter(ExpenseReportActivity.this, erList);
        liste.setAdapter(adapter);

/* Gestion du clic sur une cellule de la ListView */
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                Intent intent3 = new Intent(ExpenseReportActivity.this, ERDetailsActivity.class);
                intent3.putExtra(EXPENSE_REPORT_CODE, adapter.getItem(position).getCode());
                intent3.putExtra(EXPENSE_REPORT_DATE, adapter.getItem(position).getDate());
                intent3.putExtra(EXPENSE_REPORT_CITY, adapter.getItem(position).getCity());
                intent3.putExtra(EXPENSE_REPORT_SUBMISSION_DATE, adapter.getItem(position).getSubmissionDate());
                startActivity(intent3);
            }
        });
    }



}

