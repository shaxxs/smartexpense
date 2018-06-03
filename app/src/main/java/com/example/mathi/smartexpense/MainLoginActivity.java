package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathi.smartexpense.model.User;
import com.example.mathi.smartexpense.network.HttpGetRequest;
import java.util.concurrent.ExecutionException;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class MainLoginActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    private User user = new User();

    private EditText login;
    private EditText pass;
    private Button bouton;

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        login = findViewById(R.id.inputLoginEmail);
        pass = findViewById(R.id.inputLoginPassword);
        bouton = findViewById(R.id.btnConnexion);

        /** Gestion click sur le bouton */
        bouton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // URL de l'API qui permet de récupérer les données d'un utilisateur
                String myURL = "http://www.gyejacquot-pierre.fr/API/public/user?userEmail=" +
                            login.getText().toString() + "&userPassword=" + pass.getText().toString();
                //String myURL = "http://10.0.2.2/API/public/user?userEmail=" +
                  //      login.getText().toString() + "&userPassword=" + pass.getText().toString();
                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    // résultat de la requete http
                    result = getRequest.execute(myURL).get();
                    //System.out.println("Retour HTTPGetRequest : " + result);
                    // si la requete n'a pas été correctement effectuée
                    if (result.equals("error")){
                        //afficher un message pour avertir de l'échec de la connexion
                        Context context = getApplicationContext();
                        CharSequence text = "Mot de Passe ou identifiant incorrect !";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    // si la requete a été correctement effectuée
                    }else {
                        // objet JSON qui contient les données de l'utilisateur
                        JSONObject userProfile = new JSONObject(result);
                        // on instancie un utilisateur avec la méthode qui transforme directement le json en User
                        user = user.jsonToUser(userProfile);
                        //on ajoute l'utilisateur à notre fichier sharedpreferences
                        SharedPreferences myPref = getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putString(LOGIN_PASS_KEY, user.toJSON());
                        editor.apply();
                        Log.v("sharedpreferences", myPref.getString(LOGIN_PASS_KEY, "{}"));
                        //afficher un message pour avertir de la réussite de la connexion
                        Context context = getApplicationContext();
                        CharSequence text = "Connexion Réussie";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent intent = new Intent(MainLoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

