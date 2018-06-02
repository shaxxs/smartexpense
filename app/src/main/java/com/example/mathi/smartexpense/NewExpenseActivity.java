package com.example.mathi.smartexpense;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.smartexpense.model.ExpenseReport;
import com.example.mathi.smartexpense.network.HttpGetRequest;
import com.example.mathi.smartexpense.network.ImageProcess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class NewExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner categorySpinner;
    private EditText departureCity;
    private TextView dateExpense;
    private TextView dateDeparture;
    private TextView dateArrival;
    private EditText arrivalCity;
    private EditText kms;
    private EditText amount;
    private EditText details;
    private EditText durationTravel;
    private String category;
    private String proofTitle;
    Bitmap bitmap;
    String ImageTag = "image_tag" ;
    String ImageName = "image_data" ;
    ProgressDialog progressDialog ;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
  
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    //permet de demander les droits video et audio
    private static String[] PERMISSIONS_STORAGE_CAMERA = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT
    };
    private ImageView mImageThumbnail;
    private Button buttonProof;
    private Uri imageUri;
    private String urlProof = "";
    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    SharedPreferences sharedPreferencesER;
    private int erCode;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newexpense);

        departureCity = (EditText) findViewById(R.id.DepartureCity);
        arrivalCity = (EditText) findViewById(R.id.ArrivalCity);
        kms = (EditText) findViewById(R.id.kms);
        amount = (EditText) findViewById(R.id.amount);
        details = (EditText) findViewById(R.id.details);
        durationTravel = (EditText) findViewById(R.id.durationTravel);
        byteArrayOutputStream = new ByteArrayOutputStream();

/** Gestion du choix de la date de la dépense **/

        // liaison avec le TextView du layout
        dateExpense = findViewById(R.id.dateExpense);
        // au clic sur la date
        dateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on instancie le calendrier et on récupère la date du jour
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                currentDate = String.valueOf(mYear+ "-"+(mMonth+1)+"-"+mDay);
                Locale.setDefault(Locale.FRANCE);
                // on ouvre une boite de dialogue qui permet de choisir la date sur un calendrier
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // au clic, on affiche dans la vue la date sélectionnée
                                dateExpense.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


/** Gestion du choix de la date de départ du trajet **/

        // liaison avec le TextView du layout
        dateDeparture = findViewById(R.id.dateDeparture);
        // au clic sur la date
        dateDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on instancie le calendrier et on récupère la date du jour
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                currentDate = String.valueOf(mYear+ "-"+(mMonth+1)+"-"+mDay);
                Locale.setDefault(Locale.FRANCE);
                // on ouvre une boite de dialogue qui permet de choisir la date sur un calendrier
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // au clic, on affiche dans la vue la date sélectionnée
                                dateDeparture.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


/** Gestion du choix de la date d'arrivée du trajet **/

        // liaison avec le TextView du layout
        dateArrival = findViewById(R.id.dateArrival);
        // au clic sur la date
        dateArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on instancie le calendrier et on récupère la date du jour
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                currentDate = String.valueOf(mYear+ "-"+(mMonth+1)+"-"+mDay);
                Locale.setDefault(Locale.FRANCE);
                // on ouvre une boite de dialogue qui permet de choisir la date sur un calendrier
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // au clic, on affiche dans la vue la date sélectionnée
                                dateArrival.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


/** Gestion du clic sur le bouton Valider */

        // on récupère les données du fichier SharedPreferences
        sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);

        if (sharedPreferencesER.contains(EXPENSE_REPORT_CODE)) {
            erCode = sharedPreferencesER.getInt(EXPENSE_REPORT_CODE, 0);
        }

        Button buttonValid = (Button) findViewById(R.id.validButton);

        // au clic sur le bouton
        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enregistrement de la nouvelle dépense dans la db
                /** Si c'est un trajet */
                if (category.equals("Trajet")) {
                    // on parse les dates au format US
                    String dateDepartureUS = "";
                    try {
                        dateDepartureUS = parseDateToUS(String.valueOf(dateDeparture.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String dateArrivalUS = "";
                    try {
                        dateArrivalUS = parseDateToUS(String.valueOf(dateArrival.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        // si les champs montant, ville de départ, d'arrivée, date de départ, d'arrivée ou kms sont vides
                        if (String.valueOf(amount.getText()).equals("") || String.valueOf(departureCity.getText()).equals("") || String.valueOf(arrivalCity.getText()).equals("") || dateDepartureUS.equals("") || dateArrivalUS.equals("") || String.valueOf(kms.getText()).equals("")) {
                            Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        } else {
                            // si la date d'arrivée ou de départ est supérieure à la date du jour
                            if (((!dateDepartureUS.equals("")) && parseDate(currentDate).before(parseDate(dateDepartureUS))) || ((!dateArrivalUS.equals("")) && parseDate(currentDate).before(parseDate(dateArrivalUS)))) {
                                Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                            } else {
                                // on remplace les espaces par des underscores
                                String departureCity_replace = String.valueOf(departureCity.getText()).replace(" ", "_");
                                String arrivalCity_replace = String.valueOf(arrivalCity.getText()).replace(" ", "_");

                                // URL de l'API qui récupère les données des notes de frais
                                String myURL="http://www.gyejacquot-pierre.fr/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity_replace+"&destinationCity="+arrivalCity_replace+"&departureDate="+dateDepartureUS+"&returnDate="+dateArrivalUS+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+erCode+"&urlProof="+urlProof+"&titleProof="+proofTitle;
                                //String myURL = "http://10.0.2.2/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration=" + String.valueOf(durationTravel.getText()) + "&departureCity=" + departureCity_replace + "&destinationCity=" + arrivalCity_replace + "&departureDate=" + dateDepartureUS + "&returnDate=" + dateArrivalUS + "&km=" + String.valueOf(kms.getText()) + "&expenseReportCodeT=" + erCode + "&urlProof=" + urlProof + "&titleProof="+proofTitle;

                                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                                HttpGetRequest getRequest = new HttpGetRequest();
                                String result = "";
                                try {
                                    // résultat de la requete http
                                    result = getRequest.execute(myURL).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                // si la requete a été correctement exécutée
                                if (result.equals("Succes")) {
                                    if (!urlProof.equals("")){
                                        UploadImageToServer();
                                    }
                                    Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée", Toast.LENGTH_SHORT).show();
                                    //Lien vers la vue Note de frais
                                    Intent intentValid = new Intent(NewExpenseActivity.this, ERDetailsActivity.class);
                                    startActivity(intentValid);
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                /** Si c'est un businessexpense */
                } else {
                    // on parse la date au format US
                    String dateExpenseUS = "";
                    try {
                        dateExpenseUS = parseDateToUS(String.valueOf(dateExpense.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        // si les champs montant ou date sont vides
                        if (String.valueOf(amount.getText()).equals("") || dateExpenseUS.equals("")) {
                            Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        } else {
                            // si la date est supérieure à la date du jour
                            if ((!dateExpenseUS.equals("")) && parseDate(currentDate).before(parseDate(dateExpenseUS))) {
                                Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                            } else {
                                // on remplace les espaces par des underscores
                                String beDetails_replace = String.valueOf(details.getText()).replace(" ", "_");

                                // URL de l'API qui récupère les données des notes de frais
                                String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense/create?expenseTotalB=" + String.valueOf(amount.getText()) + "&businessExpenseLabel=" + category + "&businessExpenseDetails=" + beDetails_replace + "&businessExpenseDate=" + dateExpenseUS + "&expenseReportCodeB=" + erCode+"&urlProof="+urlProof+"&titleProof="+proofTitle;
                                //String myURL = "http://10.0.2.2/API/public/businessexpense/create?expenseTotalB=" + String.valueOf(amount.getText()) + "&businessExpenseLabel=" + category + "&businessExpenseDetails=" + beDetails_replace + "&businessExpenseDate=" +dateExpenseUS + "&expenseReportCodeB=" + erCode + "&urlProof=" + urlProof + "&titleProof="+proofTitle;
                                // on instancie la classe HttpGetRequest qui permet de créer la requete HTTP avec l'url de l'API
                                HttpGetRequest getRequest = new HttpGetRequest();
                                String result = "";
                                try {
                                    // résultat de la requete http
                                    result = getRequest.execute(myURL).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                // si la requete a été correctement exécutée
                                if (result.equals("Succes")) {
                                    if (!urlProof.equals("")){
                                        UploadImageToServer();
                                    }
                                    Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée", Toast.LENGTH_SHORT).show();
                                    //Lien vers la vue Note de frais
                                    Intent intentValid = new Intent(NewExpenseActivity.this, ERDetailsActivity.class);
                                    startActivity(intentValid);
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

/** Gestion du clic sur le bouton Ajouter un justificatif */
        buttonProof = findViewById(R.id.btnAddProof);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proofTitle = "justificatif_" + String.valueOf(System.currentTimeMillis());
                urlProof = "http://www.gyejacquot-pierre.fr/API/src/Resources/upload/" +proofTitle + ".jpg";
                int permission = ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
// Nous n'avons pas la permission de stocker dans le
// PackageManager pour READ et WRITE sur external storage + Utiliser LA CAMERA
                    ActivityCompat.requestPermissions(
                            NewExpenseActivity.this, PERMISSIONS_STORAGE_CAMERA,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                    );
                } else {
                    takePictureIntent();
                }
            }
        });

/** Gestion du clic sur le bouton Retour */
        Button buttonReturn = (Button) findViewById(R.id.returnButton);
        buttonReturn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
            //Lien vers la vue Note de Frais Détails
            Intent intentReturn = new Intent(NewExpenseActivity.this, ERDetailsActivity.class);
            startActivity(intentReturn);
            }
        });

/** Ajout d'un écouteur d'évènement sur la liste déroulante */
        addListenerOnSpinnerItemSelection();

    }

/** Traitement de la prise de vue par l'appareil photo en l'appelant et en enregistrant la photo dans le lien envoyé via putextra */
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        imageUri = Uri.fromFile(new
                File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM), "justificatif_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

/** Lorsque la photo a été prise et la photo enregistrée on peut appeler la photo pour la mettre dans une image view */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                retrieveCapturedPicture();
            }
        }
    }

/** Pour traiter la photo qui a été prise pour le justificatif en l'envoyant vers l'image view mImageThumbnail */
    private void retrieveCapturedPicture() {
        // display picture on ImageView
        ImageView imageView = findViewById(R.id.mImageThumbnail);
        imageView.setVisibility(View.VISIBLE);
        Button btnAddProof = findViewById(R.id.btnAddProof);
        btnAddProof.setVisibility(View.GONE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);
    }

/** Pour traiter l'envoi de l'image sur le serveur */
    public void UploadImageToServer(){

        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(NewExpenseActivity.this,"Image en cours d'envoi","Veuillez patienter",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toast.makeText(NewExpenseActivity.this,"Image envoyée",Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcess imageProcessClass = new ImageProcess();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageTag, proofTitle);

                HashMapParams.put(ImageName, ConvertImage);

                //String FinalData = imageProcessClass.ImageHttpRequest("http://10.0.2.2/API/public/upload", HashMapParams);
                String FinalData = imageProcessClass.ImageHttpRequest("http://www.gyejacquot-pierre.fr/API/public/upload", HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

/** Méthode qui ajoute un écouteur d'évènement à la liste déroulante */
    public void addListenerOnSpinnerItemSelection() {
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(this);
    }

/** Gestion de l'item sélectionné dans la liste déroulante et des champs affichés sur la vue */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        category = parent.getItemAtPosition(pos).toString();
        // si c'est un trajet
        if (category.equals("Trajet")) {
            // on affiche les champs en Visible et on cache ceux en Gone
            departureCity.setVisibility(View.VISIBLE);
            dateExpense.setVisibility(View.GONE);
            dateDeparture.setVisibility(View.VISIBLE);
            dateArrival.setVisibility(View.VISIBLE);
            arrivalCity.setVisibility(View.VISIBLE);
            kms.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
            durationTravel.setVisibility(View.VISIBLE);
            departureCity.setVisibility(View.VISIBLE);
        // si c'est une autre dépense
        } else {
            // on affiche les champs en Visible et on cache ceux en Gone
            departureCity.setVisibility(View.GONE);
            dateExpense.setVisibility(View.VISIBLE);
            dateDeparture.setVisibility(View.GONE);
            dateArrival.setVisibility(View.GONE);
            arrivalCity.setVisibility(View.GONE);
            kms.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
            durationTravel.setVisibility(View.GONE);
            departureCity.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

/** fonction qui parse un String en Date */
    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = format.parse(date);
        return newDate;
    }

    /** fonction qui change la Date format FR au format US */
    public String parseDateToUS(String date) throws ParseException {
        SimpleDateFormat formatFR = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatUS = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFR = formatFR.parse(date);
        String dateUS = formatUS.format(dateFR);
        return dateUS;
    }
}
