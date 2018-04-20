package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.model.ExpenseAdapter;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RefundTrackerActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    private ListView listViewRefunds;
    ExpenseAdapter adapter;
    private Spinner monthSpinner;
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String EXPENSE_LABEL = "expense_label";
    final String EXPENSE_ID = "expense_id";
    final String REFUND_TRACKER = "refund_tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_tracker);

       /* Récupération des données de SharedPreferences */
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

        // Stockage de l'Id utilisateur récupéré via SharedPreferences
        int idUser = 0;
        try {
            if (userProfile != null) {
                idUser = userProfile.getInt("idUser");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Gestion du clic sur le bouton retour */
        Button buttonReturn = findViewById(R.id.returnButton);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intentReturn = new Intent(RefundTrackerActivity.this, DashboardActivity.class);
                startActivity(intentReturn);
            }
        });

        /* Gestion de la ListView */
        listViewRefunds = findViewById(R.id.listViewRefunds);
        List<Expense> eList = new ArrayList<Expense>();
        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses/user?idUser="+idUser;
        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expenses/user?idUser="+idUser;
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            String result = getRequest.execute(myURL).get();
            System.out.println("Retour HTTPGetRequest : " + result);
            JSONArray array = new JSONArray(result);
            for (int i= 0; i < array.length(); i++) {
                JSONObject obj = new JSONObject(array.getString(i));
                if (!obj.getString("refundAmount").equals("null")) {
                    String comment = "";
                    if (obj.getString("expenseDetails").equals("null")) {
                        comment = "";
                    } else {
                        comment = obj.getString("expenseDetails");
                    }
                    eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, obj.getInt("refundAmount")));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ExpenseAdapter(RefundTrackerActivity.this, eList);
        listViewRefunds.setAdapter(adapter);

/* Gestion du clic sur une cellule de la ListView */
        listViewRefunds.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

            SharedPreferences sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, Context.MODE_PRIVATE);
            sharedPreferencesER.edit()
                    .putString(EXPENSE_LABEL, adapter.getItem(position).getLabel())
                    .putInt(EXPENSE_ID, adapter.getItem(position).getIdExpense())
                    .putBoolean(REFUND_TRACKER, true)
                    .apply();
            Intent intentNextPage = new Intent(RefundTrackerActivity.this, ExpenseDetailsActivity.class);
            startActivity(intentNextPage);
            }
        });
    }
}
