package com.example.mathi.smartexpense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.network.ExpenseAdapter;
import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class ERDetailsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferencesER;
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String EXPENSE_REPORT_KEY = "expense_report";
    final String EXPENSE_KEY = "expense";
    final String REFUND_TRACKER = "refund_tracker";

    private ExpenseReport er = new ExpenseReport();

    private ListView liste;
    private ExpenseAdapter adapter;
    private TextView status;
    TextView date;
    TextView code;
    TextView city;
    Button returnButton;
    private Button addButton;
    private Button submitButton;

    String jsonExpenseReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erdetails);

        status = (TextView) findViewById(R.id.ERStatus);
        code = (TextView) findViewById(R.id.numberER);
        date = (TextView) findViewById(R.id.ERDate);
        city = (TextView) findViewById(R.id.ERCity);
        returnButton = (Button) findViewById(R.id.returnButtonERDetails);
        addButton = (Button) findViewById(R.id.addExpenseButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        liste = findViewById(R.id.listERDetails);

        /** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_REPORT_KEY)) {
            jsonExpenseReport = sharedPreferencesER.getString(EXPENSE_REPORT_KEY, null);
            JSONObject json = null;
            try {
                json = new JSONObject(jsonExpenseReport);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            er = er.jsonToExpenseReport(json);
        }

        /** Injection dans les TextView de la vue des données de la note de frais */
        code.setText("Note de frais n°" + er.getExpenseReportCode());
        date.setText(er.getExpenseReportDate());
        city.setText(er.getExpenseReportCity());
        if (er.getSubmissionDate().equals("")) {
            status.setText("Non soumise");
        } else {
            status.setText("Soumise le " + er.getSubmissionDate());
        }

        /** si la note a déjà été soumise, on cache les boutons Ajouter une dépense et Soumettre */
        if (er.getSubmissionDate().equals("")) {
            addButton.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
            // sinon, on affiche le bouton
        } else {
            addButton.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
        }

        /** Méthode qui remplit la ListView */
        setListView();

        /** Gestion du clic sur une cellule de la ListView */
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
                // on ajoute les données de la cellule (dépense) cliquée au fichier SharedPreferences
                sharedPreferencesER.edit()
                        .putString(EXPENSE_KEY, adapter.getItem(position).toJSON())
                        .putBoolean(REFUND_TRACKER, false)
                        .apply();
                // lien vers la page Dépense - Détails
                Intent intentNextPage = new Intent(ERDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intentNextPage);
            }
        });

        /** Gestion du clic sur le bouton Soumettre */
        // au clic sur le bouton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // URL de l'API qui permet la mise à jour de la note de frais dans la db : création de la date de soumission + état de validation à En cours pour les dépenses
                String myURL = "http://www.gyejacquot-pierre.fr/API/public/expensereport/update?expenseReportCode="+er.getExpenseReportCode();
                //String myURL = "http://10.0.2.2/API/public/expensereport/update?expenseReportCode="+er.getExpenseReportCode();
                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    // résultat de la requete http
                    String result = getRequest.execute(myURL).get();
                    // si la requete a été correctement effectuée
                    if (!result.isEmpty()) {
                        // on affiche un toast
                        Toast.makeText(getApplicationContext(), "Note de frais soumise",Toast.LENGTH_SHORT).show();
                        // On récupère la date de soumission qu'on vient de créer
                        JSONObject obj = new JSONObject(result);
                        er.setSubmissionDate(obj.getString("submissionDate"));
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
                status.setText("Soumise le " + er.getSubmissionDate());
                // On cache le bouton Soumettre et Ajout dépense
                submitButton.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                // Mise à jour de la ListView
                setListView();
                // Mise à jour du fichier SharedPreferences (modification de la date de soumission)
                sharedPreferencesER.edit()
                        .putString(EXPENSE_REPORT_KEY, er.toJSON())
                        .apply();
            }
        });

        /** Gestion du clic sur le bouton Retour */
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lien vers la vue Notes de frais
                Intent intentReturn = new Intent(ERDetailsActivity.this, ExpenseReportActivity.class);
                startActivity(intentReturn);
            }
        });

        /** Gestion du clic sur le bouton Ajouter une dépense */
        // au clic sur le bouton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lien vers la vue Nouvelle dépense
                Intent intentAddExpense = new Intent(ERDetailsActivity.this, NewExpenseActivity.class);
                startActivity(intentAddExpense);
            }
        });
    }

    /** Méthode qui remplit la ListView */
    public void setListView() {
        // liste des dépenses qui seront injectées dans la ListView
        List<Expense> eList = new ArrayList<Expense>();
        // URL de l'API qui récupère les données des dépenses de la note de frais
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses/er?expenseReportCode="+er.getExpenseReportCode();
        //String myURL = "http://10.0.2.2/API/public/expenses/er?expenseReportCode="+er.getExpenseReportCode();
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
                String comment = "";
                // si le champ expenseDetails de la dépense est vide
                if (obj.isNull("expenseDetails")) {
                    comment = "";
                } else {
                    comment = obj.getString("expenseDetails");
                }
                String submissionDate = "";
                if (obj.isNull("submissionDate")){
                    submissionDate = "";
                } else {
                    submissionDate = obj.getString("submissionDate");
                }
                // on ajoute la dépense à la liste
                eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, Float.parseFloat(obj.getString("expenseTotal")), submissionDate));
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
        // génère les cellules de la ListView
        adapter = new ExpenseAdapter(ERDetailsActivity.this, eList);
        // affecte les cellules à notre ListView
        liste.setAdapter(adapter);
    }
}
