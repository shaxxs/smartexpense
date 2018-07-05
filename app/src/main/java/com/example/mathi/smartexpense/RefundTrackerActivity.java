package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mathi.smartexpense.model.Expense;
import com.example.mathi.smartexpense.network.ExpenseAdapter;
import com.example.mathi.smartexpense.model.User;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class RefundTrackerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    final String EXPENSE_KEY = "expense";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    final String REFUND_TRACKER = "refund_tracker";

    private User user = new User();

    private Spinner monthSpinner;
    Button buttonReturn;
    ListView listViewRefunds;

    private ExpenseAdapter adapter;
    private JSONArray array;
    private List<Expense> eList;
    private List<String> months = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_tracker);

        buttonReturn = findViewById(R.id.returnButton);
        listViewRefunds = findViewById(R.id.listViewRefunds);
        monthSpinner = (Spinner) findViewById(R.id.monthsSpinner);

        addListenerOnSpinnerItemSelection();

        /** Récupération des données du fichier SharedPreferences */
        SharedPreferences myPref = this.getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
        String user_profile = myPref.getString(LOGIN_PASS_KEY, "{}");
        Log.v("shared_preferences", user_profile);
        JSONObject userProfile = null;
        try {
            userProfile = new JSONObject(user_profile);
            user = user.jsonToUser(userProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /** Gestion de la ListView */

        // liste des dépenses qui seront injectées dans la ListView
        eList = new ArrayList<Expense>();
        // URL de l'API qui récupère les données des dépenses
        String myURL = "http://www.gyejacquot-pierre.fr/API/public/expenses/user?idUser="+user.getIdUser();
        //String myURL = "http://10.0.2.2/API/public/expenses/user?idUser="+user.getIdUser();
        // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            // résultat de la requete http
            String result = getRequest.execute(myURL).get();
            // tableau JSON qui contient le résultat
            array = new JSONArray(result);

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        /** Gestion du clic sur une cellule de la ListView */
        listViewRefunds.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

            // On ajoute les données de la dépense à notre fichier sharedpreferences
            SharedPreferences sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, Context.MODE_PRIVATE);
            sharedPreferencesER.edit()
                    .putString(EXPENSE_KEY, adapter.getItem(position).toJSON())
                    .putBoolean(REFUND_TRACKER, true)
                    .apply();
            // lien vers la page Dépense - Détails
            Intent intentNextPage = new Intent(RefundTrackerActivity.this, ExpenseDetailsActivity.class);
            startActivity(intentNextPage);
            }
        });

        /** Gestion du filtre par mois **/
        // on récupère le numéro du mois en cours
        final Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH); // current month

        // on créé une liste des 5 derniers mois
        for (int i = mMonth-4; i <= mMonth; i++){
            months.add(getMonthName(i, Locale.FRANCE, false));
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapterSpinner);

        /** Gestion du clic sur le bouton retour */
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intentReturn = new Intent(RefundTrackerActivity.this, DashboardActivity.class);
                startActivity(intentReturn);
            }
        });
    }

    /** Retourne le nom d'un mois en fonction de son numéro */
    private String getMonthName(final int index, final Locale locale, final boolean shortName)
    {
        String format = "%tB";

        if (shortName)
            format = "%tb";

        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(Calendar.MONTH, index);

        return String.format(locale, format, calendar);
    }

    /** fonction qui change la Date format US au format mois */
    public String parseDateToMonth(String date) throws ParseException {
        SimpleDateFormat formatUS = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        Date dateUS = formatUS.parse(date);
        String dateMonth = formatMonth.format(dateUS);
        return dateMonth;
    }

    /** Gestion de la sélection sur la liste déroulante */
    private void addListenerOnSpinnerItemSelection() {
        monthSpinner.setOnItemSelectedListener(this);
    }

    /** Gestion de l'item sélectionné dans la liste déroulante */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String monthSelected = (String) adapterView.getItemAtPosition(i);
        eList = new ArrayList<Expense>();

        // boucle sur la longueur du tableau JSON qui contient le résultat de la requete
        try {
            for (int j= 0; j < array.length(); j++) {
                // à chaque tour de boucle, on récupère un objet JSON du tableau, qui contient les données d'une dépense, 1 dépense = 1 objet
                JSONObject obj = null;

                    obj = new JSONObject(array.getString(j));

                // si le champ refundAmount de la dépense n'est pas vide
                if (!obj.isNull("refundAmount")) {
                    String comment = "";
                    // si le champ expenseDetails de la dépense est vide
                    if (obj.isNull("expenseDetails")) {
                        comment = "";
                    } else {
                        comment = obj.getString("expenseDetails");
                    }
                    int monthNumber = Integer.parseInt(parseDateToMonth(obj.getString("expenseDate")));
                    String monthName = getMonthName(monthNumber-1, Locale.FRANCE, false);
                    // on ajoute la dépense à la liste
                    if (monthName.equals(monthSelected)) {
                        eList.add(new Expense(obj.getInt("idExpense"), obj.getString("expenseDate"), obj.getString("expenseLabel"), comment, Float.parseFloat(obj.getString("refundAmount")), obj.getString("submissionDate")));
                    }
                }
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        // génère les cellules de la ListView
        adapter = new ExpenseAdapter(RefundTrackerActivity.this, eList);
        // affecte les cellules à notre ListView
        listViewRefunds.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
