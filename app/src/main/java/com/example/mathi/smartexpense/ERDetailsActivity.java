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
import com.example.mathi.smartexpense.model.ExpenseAdapter;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ERDetailsActivity extends AppCompatActivity {

    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String EXPENSE_REPORT_DATE = "expense_report_date";
    final String EXPENSE_REPORT_CITY = "expense_report_city";
    final String EXPENSE_REPORT_SUBMISSION_DATE = "expense_report_submission_date";
    private ListView liste;
    private ExpenseAdapter adapter;
    final String EXPENSE_LABEL = "expense_label";
    final String EXPENSE_ID = "expense_id";
    private TextView status;
    SharedPreferences sharedPreferencesER;
    private String erDate;
    private String erCity;
    private int erCode;
    private String erSubmissionDate;
    final String REFUND_TRACKER = "refund_tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erdetails);

        status = (TextView) findViewById(R.id.ERStatus);
        liste = findViewById(R.id.listERDetails);

        /** on récupère les données de notre fichier SharedPreferences */
        sharedPreferencesER = this.getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
        if (sharedPreferencesER.contains(EXPENSE_REPORT_CODE) && sharedPreferencesER.contains(EXPENSE_REPORT_CITY) && sharedPreferencesER.contains(EXPENSE_REPORT_DATE)) {
            erDate = sharedPreferencesER.getString(EXPENSE_REPORT_DATE, null);
            erCity = sharedPreferencesER.getString(EXPENSE_REPORT_CITY, null);
            erCode = sharedPreferencesER.getInt(EXPENSE_REPORT_CODE, 0);
            erSubmissionDate = sharedPreferencesER.getString(EXPENSE_REPORT_SUBMISSION_DATE, null);
        }

        /** Gestion du clic sur le bouton Retour */
        Button returnButton = (Button) findViewById(R.id.returnButtonERDetails);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lien vers la vue Notes de frais
                Intent intentReturn = new Intent(ERDetailsActivity.this, ExpenseReportActivity.class);
                startActivity(intentReturn);
            }
        });

        /** Gestion du clic sur le bouton Ajouter une dépense */
        Button addButton = (Button) findViewById(R.id.addExpenseButton);
        // si la note a déjà été soumise, on cache le bouton Ajouter une dépense
        if (erSubmissionDate.equals("null") || erSubmissionDate.equals("")) {
            addButton.setVisibility(View.VISIBLE);
        // sinon, on affiche le bouton
        } else {
            addButton.setVisibility(View.GONE);
        }
        // au clic sur le bouton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lien vers la vue Nouvelle dépense
                Intent intentAddExpense = new Intent(ERDetailsActivity.this, NewExpenseActivity.class);
                startActivity(intentAddExpense);
            }
        });

        /** Gestion du clic sur le bouton Soumettre */
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        // si la note a déjà été soumise, on cache le bouton Soumettre
        if (erSubmissionDate.equals("null") || erSubmissionDate.equals("")) {
            submitButton.setVisibility(View.VISIBLE);
        // sinon, on affiche le bouton
        } else {
            submitButton.setVisibility(View.GONE);
        }
        // au clic sur le bouton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submissionDate = "";
                // URL de l'API qui permet la mise à jour de la note de frais dans la db : création de la date de soumission + état de validation à En cours pour les dépenses
                String myURL = "http://www.gyejacquot-pierre.fr/API/public/expensereport/update?expenseReportCode="+erCode;
                //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expensereport/update?expenseReportCode="+erCode;
                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    // résultat de la requete http
                    String result =getRequest.execute(myURL).get();
                    // si la requete a été correctement effectuée
                    if (!result.isEmpty()) {
                        // on affiche un toast
                        Toast.makeText(getApplicationContext(), "Note de frais soumise",Toast.LENGTH_SHORT).show();
                        // On récupère la date de soumission qu'on vient de créer pour l'injecter dans le TextView Soumise le ...
                        JSONObject obj = new JSONObject(result);
                        submissionDate = obj.getString("submissionDate");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status.setText("Soumise le "+submissionDate);
                // On cache le bouton Soumettre
                submitButton.setVisibility(View.GONE);
                // Mise à jour de la ListView
                setListView();
                // Mise à jour du fichier SharedPreferences : ajout de la date de soumission
                sharedPreferencesER.edit()
                        .putString(EXPENSE_REPORT_SUBMISSION_DATE, submissionDate)
                        .apply();

            }
        });

        /** Injection dans les TextView de la vue des données du fichier SharedPreferences */
        TextView code = (TextView) findViewById(R.id.numberER);
        code.setText("Note de frais n°"+erCode);

        TextView date = (TextView) findViewById(R.id.ERDate);
        date.setText(erDate);

        if (erSubmissionDate.equals("null") || erSubmissionDate.equals("")) {
            status.setText("Non soumise");
        } else {
            status.setText("Soumise le "+erSubmissionDate);
        }

        TextView city = (TextView) findViewById(R.id.ERCity);
        city.setText(erCity);

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
                        .putString(EXPENSE_LABEL, adapter.getItem(position).getLabel())
                        .putInt(EXPENSE_ID, adapter.getItem(position).getIdExpense())
                        .putBoolean(REFUND_TRACKER, false)
                        .apply();
                // lien vers la page Dépense - Détails
                Intent intentNextPage = new Intent(ERDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intentNextPage);
            }
        });
    }

    /** Méthode qui remplit la ListView */
    public void setListView() {
        // liste des dépenses qui seront injectées dans la ListView
        List<Expense> eList = new ArrayList<Expense>();
        // URL de l'API qui récupère les données des dépenses de la note de frais
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses/er?expenseReportCode="+erCode;
        //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/expenses/er?expenseReportCode="+erCode;
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
                // on ajoute la dépense à la liste
                eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, Float.parseFloat(obj.getString("expenseTotal")), obj.getString("submissionDate")));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // génère les cellules de la ListView
        adapter = new ExpenseAdapter(ERDetailsActivity.this, eList);
        // affecte les cellules à notre ListView
        liste.setAdapter(adapter);
    }
}
