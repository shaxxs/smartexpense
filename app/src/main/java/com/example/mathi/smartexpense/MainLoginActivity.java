package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class MainLoginActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button connectionButton;
    // String pour stocker le login saisi
    private String login;
    // String pour stocker le mdp saisi
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        inputLogin = (EditText) findViewById(R.id.inputLoginEmail);
        inputPassword = (EditText) findViewById(R.id.inputLoginPassword);
        connectionButton = (Button) findViewById(R.id.conButton);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stockage du login entré par l'utilisateur
                login = inputLogin.getText().toString();
                // Stockage du mot de passe entré par l'utilisateur
                password = inputPassword.getText().toString();
                readJson();
            }
        });
    }

    // récupère le fichier Json, le parcoure et vérifie si le mdp et le login sont corrects
    private void readJson() {
        String myUrl = "http://10.0.2.2/testapi/index.php";
        // String pour stocker la réponse du serveur (JSON)
        String result = "";
        // Nouvelle instance de la classe dédiée aux requests HTTP
        HttpGetRequest getRequest = new HttpGetRequest("GET");
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
            // Parcoure le tableau d'objets Json : 1 utilisateur à chaque tour de boucle (mail+mdp)
            for (int i= 0; i < array.length(); i++) {
                // Instanciation d'un objet Json contenant les infos d'un utilisateur i
                JSONObject obj = new JSONObject(array.getString(i));
                // String stockage du mail de l'utilisateur i du Json
                String userEmail = obj.getString("userEmail");
                // String stockage du mdp de l'utilisateur i du Json
                String userPassword = obj.getString("userPassword");
                // vérifie si le login et le mdp saisis correspondent à un utilisateur du Json
                // si oui, instance d'un intent vers le dashboard
                if (userEmail.equals(login) && userPassword.equals(password)){
                    Intent dashboardActivity = new Intent(MainLoginActivity.this, DashboardActivity.class);
                    startActivity(dashboardActivity);
                    i = array.length();
                // si non, toast Erreur d'authentification
                } else if (!userEmail.equals(login) && !userPassword.equals(password) && i==array.length()-1) {
                    Toast.makeText(this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
