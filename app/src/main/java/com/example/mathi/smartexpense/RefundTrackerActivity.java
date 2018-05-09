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

/** Récupération des données du fichier SharedPreferences */
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

/** Gestion du clic sur le bouton retour */
        Button buttonReturn = findViewById(R.id.returnButton);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intentReturn = new Intent(RefundTrackerActivity.this, DashboardActivity.class);
                startActivity(intentReturn);
            }
        });

/** Gestion de la ListView */
        listViewRefunds = findViewById(R.id.listViewRefunds);
        // liste des dépenses qui seront injectées dans la ListView
        List<Expense> eList = new ArrayList<Expense>();
        // URL de l'API qui récupère les données des dépenses
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses/user?idUser="+idUser;
        //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expenses/user?idUser="+idUser;
        // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            // résultat de la requete http
            String result = getRequest.execute(myURL).get();
            // tableau JSON qui contient le résultat
            JSONArray array = new JSONArray(result);
            // boucle sur la longueur du tableau JSON qui contient le résultat de la requete
            for (int i= 0; i < array.length(); i++) {
                // à chaque tour de boucle, on récupère un objet JSON du tableau, qui contient les données d'une dépense, 1 dépense = 1 objet
                JSONObject obj = new JSONObject(array.getString(i));
                // si le champ refundAmount de la dépense n'est pas vide
                if (!obj.isNull("refundAmount")) {
                    String comment = "";
                    // si le champ expenseDetails de la dépense est vide
                    if (obj.isNull("expenseDetails")) {
                        comment = "";
                    } else {
                        comment = obj.getString("expenseDetails");
                    }
                    // on ajoute la dépense à la liste
                    eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, Float.parseFloat(obj.getString("refundAmount")), obj.getString("submissionDate")));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // génère les cellules de la ListView
        adapter = new ExpenseAdapter(RefundTrackerActivity.this, eList);
        // affecte les cellules à notre ListView
        listViewRefunds.setAdapter(adapter);

/** Gestion du clic sur une cellule de la ListView */
        listViewRefunds.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

            // On ajoute les données de la dépense à notre fichier sharedpreferences
            SharedPreferences sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, Context.MODE_PRIVATE);
            sharedPreferencesER.edit()
                    .putString(EXPENSE_LABEL, adapter.getItem(position).getLabel())
                    .putInt(EXPENSE_ID, adapter.getItem(position).getIdExpense())
                    .putBoolean(REFUND_TRACKER, true)
                    .apply();
            // lien vers la page Dépense - Détails
            Intent intentNextPage = new Intent(RefundTrackerActivity.this, ExpenseDetailsActivity.class);
            startActivity(intentNextPage);
            }
        });
    }
}
