package com.example.mathi.smartexpense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mathi.smartexpense.network.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
=======
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
>>>>>>> f5c171f324f326b7c3c1e605957e44bb923fc973

public class MainLoginActivity extends AppCompatActivity {
    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";
    String result;

    private EditText inputLogin;
    private EditText inputPassword;
    private Button connexionButton;
    private TextView forgotPasswordLink;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        final EditText login = findViewById(R.id.inputLoginEmail);
        final EditText pass = findViewById(R.id.inputLoginPassword);

        /* Gestion click sur le bouton */
        Button bouton = findViewById(R.id.btnConnexion);
        bouton.setOnClickListener(new View.OnClickListener() {
            public static final String FILE_PROFILE = "file_user_profile";

            @Override
            public void onClick(View v) {
                String myURL = "http://www.gyejacquot-pierre.fr/loggin.php?email=" +
                        login.getText().toString() + "&mdp=" + pass.getText().toString();
                HttpGetRequest getRequest = new HttpGetRequest();
                try {
                    result = getRequest.execute(myURL).get();
                    //on ajoute les identifiants à notre fichier sharedpreferences
                    SharedPreferences myPref = getSharedPreferences(FILE_PROFILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString(LOGIN_PASS_KEY, result);
                    editor.apply();
                    Log.v("sharedpreferences", myPref.getString(LOGIN_PASS_KEY, "{}"));
                    Intent intent = new Intent(MainLoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Retour HTTPGetRequest : " + result);
                try {JSONArray user = new JSONArray(result); } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

