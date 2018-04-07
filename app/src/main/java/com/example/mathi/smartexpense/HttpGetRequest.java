package com.example.mathi.smartexpense;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mathi on 07/04/2018.
 */

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    public static String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private HttpURLConnection connection;

    public HttpGetRequest(String REQUEST_METHOD) {
        this.REQUEST_METHOD = REQUEST_METHOD;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        try {
            //Création d’un objet URL pour accéder àl’URL
            URL myUrl = new URL(stringUrl);
            //Create a connection
            connection = (HttpURLConnection) myUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return getDataFromServer(); // JSON renvoyé
    }

    protected String getDataFromServer() {
        String result;
        String inputLine;

        try {
            //gestion d’entête HTTP (method, timeout, ...
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            //Connexion effective à l’UTL
            connection.connect();
            //Création d’un flux de lecture en entrée
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            //Création dBuffer de lecture et StringBuilder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Vérif si contenu d’un ligne (\n) est null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            //Fermeture des flux (buffer et flux d’entrée
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        } catch(IOException e)

        {
            e.printStackTrace();
            result = null;
        }
        return result; // JSON reçu du serveur
    }
}
