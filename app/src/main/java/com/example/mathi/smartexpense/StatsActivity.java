package com.example.mathi.smartexpense;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.mathi.smartexpense.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre Gyejacquot, Ahmed Hamad and Mathilde Person.
 */

public class StatsActivity extends AppCompatActivity {

    final String LOGIN_PASS_KEY = "user_profile";
    final String FILE_PROFILE = "file_user_profile";

    private User user = new User();

    Button bouton;
    WebView w;
    WebView w2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        bouton = findViewById(R.id.returnButton);
        w = (WebView) findViewById(R.id.statPie);
        w2 = (WebView) findViewById(R.id.statColumn);


        /** Récupération des données stockées dans le fichier SharedPreferences */
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

        /** Gestion du clic sur le bouton retour */
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intent = new Intent(StatsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        /** Gestion de la WebView qui affiche le camembert */
        w.getSettings().setJavaScriptEnabled(true);
        final Activity activity = this;

        w.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        w.getSettings().setLoadWithOverviewMode(true);
        w.getSettings().setUseWideViewPort(true);
        // la webview charge l'URL de l'API qui affiche le camembert
        w.loadUrl("http://www.gyejacquot-pierre.fr/API/public/piechart?idUser="+user.getIdUser());
        //w.loadUrl("http://10.0.2.2/API/public/piechart?idUser="+user.getIdUser());

        /** Gestion de la WebView qui affiche les stats en colonnes */
        w2.getSettings().setJavaScriptEnabled(true);
        final Activity activity2 = this;

        w2.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity2, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        w2.getSettings().setLoadWithOverviewMode(true);
        w2.getSettings().setUseWideViewPort(true);
        // la webview charge l'URL de l'API qui affiche les colonnes
        w2.loadUrl("http://www.gyejacquot-pierre.fr/API/public/columnchart?idUser="+user.getIdUser());
        //w2.loadUrl("http://10.0.2.2/API/public/columnchart?idUser="+user.getIdUser());
    }
}
