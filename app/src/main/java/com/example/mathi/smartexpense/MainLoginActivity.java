package com.example.mathi.smartexpense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class MainLoginActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button connectionButton;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        inputLogin = (EditText) findViewById(R.id.inputLoginEmail);
        inputPassword = (EditText) findViewById(R.id.inputLoginPassword);
        connectionButton = (Button) findViewById(R.id.conButton);
        connectionButton.setEnabled(false);
        login = inputLogin.getText().toString();
        password = inputPassword.getText().toString();

        String myUrl = "http://10.0.2.2/testapi/index.php"; // en ligne sur Internet
        //String pour stocker la réponse du serveur (JSON)
        String result="";
        //Instantiation d'une nouvelle instance de notre classe dédiée aux requests HTTP
        HttpGetRequest getRequest = new HttpGetRequest("GET");
        //Exécute la fonction doInBackground, de la classe HttpGetRequest en passant l'url
        try {
            result = getRequest.execute(myUrl).get(); // Exécution du Thread pour connexion.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            // On récupère le JSON complet
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = new JSONArray(jsonObject);
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                // On fait le lien avec l'Objet JSON
                if ((login.equals(obj.getString("userEmail")) && (password.equals(obj.getString("userPassword"))){
                    connectionButton.setEnabled(true);
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }


}
