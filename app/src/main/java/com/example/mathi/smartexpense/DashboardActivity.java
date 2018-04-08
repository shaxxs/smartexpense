package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DashboardActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView company;
    private TextView email;
    // String pour stocker les infos de l'utilisateur contenues dans le Json
    private String firstNameFromJson;
    private String lastNameFromJson;
    private String companyFromJson;
    private String emailFromJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firstName = (TextView) findViewById(R.id.labelFirstName);
        lastName = (TextView) findViewById(R.id.labelLastName);
        company = (TextView) findViewById(R.id.labelCompany);
        email = (TextView) findViewById(R.id.labelEmail);
        readJson();
        // affichage des infos de l'utilisateur récupérées dans le Json
        firstName.setText(firstNameFromJson);
        lastName.setText(lastNameFromJson);
        company.setText(companyFromJson);
        email.setText(emailFromJson);
    }

    // récupère le fichier Json, le parcoure et stocke les infos dans des variables
    private void readJson() {
        String myUrl = "http://10.0.2.2/testapi/index.php";
        // String pour stocker la réponse du serveur (JSON)
        String result = "";
        // Nouvelle instance de la classe dédiée aux requests HTTP
        HttpGetRequest getRequest = new HttpGetRequest();
        // Execute la fonction doInBackground, de la classe HttpGetRequest en passant l'url
        try {
            // Execution du Thread pour connexion
            result = getRequest.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            // Array stocke le tableau d'objets du fichier Json
            JSONArray array = new JSONArray(result);
            // Instanciation d'un objet Json contenant les infos de l'utilisateur de l'array
            JSONObject obj = new JSONObject(array.getString(0));
            // String stockage des infos de l'utilisateur du Json
            firstNameFromJson = obj.getString("userFirstName");
            lastNameFromJson = obj.getString("userLastName");
            companyFromJson = obj.getString("corporateName");
            emailFromJson = obj.getString("userEmail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
