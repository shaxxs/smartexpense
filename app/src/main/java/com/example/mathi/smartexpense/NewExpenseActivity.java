package com.example.mathi.smartexpense;


import android.Manifest;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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
                                dateExpense.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
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
                                dateDeparture.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
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
                                dateArrival.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
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
                    try {
                        // si les champs montant, ville de départ, d'arrivée, date de départ, d'arrivée ou kms sont vides
                        if (String.valueOf(amount.getText()).equals("") || String.valueOf(departureCity.getText()).equals("") || String.valueOf(arrivalCity.getText()).equals("") || String.valueOf(dateDeparture.getText()).equals("") || String.valueOf(dateArrival.getText()).equals("") || String.valueOf(kms.getText()).equals("")) {
                            Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        } else {
                            // si la date d'arrivée ou de départ est supérieure à la date du jour
                            if (((!String.valueOf(dateDeparture).equals("")) && parseDate(currentDate).before(parseDate(String.valueOf(dateDeparture.getText())))) || ((!String.valueOf(dateArrival).equals("")) && parseDate(currentDate).before(parseDate(String.valueOf(dateArrival.getText()))))) {
                                Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                            } else {
                                // URL de l'API qui récupère les données des notes de frais
                                String myURL="http://www.gyejacquot-pierre.fr/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity.getText()+"&destinationCity="+arrivalCity.getText()+"&departureDate="+dateDeparture.getText()+"&returnDate="+dateArrival.getText()+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+erCode+"&urlProof="+urlProof+"&titleProof=justificatif_"+String.valueOf(System.currentTimeMillis());
                                //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration=" + String.valueOf(durationTravel.getText()) + "&departureCity=" + departureCity.getText() + "&destinationCity=" + arrivalCity.getText() + "&departureDate=" + dateDeparture.getText() + "&returnDate=" + dateArrival.getText() + "&km=" + String.valueOf(kms.getText()) + "&expenseReportCodeT=" + erCode + "&urlProof=" + urlProof + "&titleProof=justificatif_" + String.valueOf(System.currentTimeMillis());

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
                    try {
                        // si les champs montant ou date sont vides
                        if (String.valueOf(amount.getText()).equals("") || String.valueOf(dateExpense.getText()).equals("")) {
                            Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        } else {
                            // si la date est supérieure à la date du jour
                            if ((!String.valueOf(dateExpense).equals("")) && parseDate(currentDate).before(parseDate(String.valueOf(dateExpense.getText())))) {
                                Toast.makeText(getApplicationContext(), "Date supérieure à la date du jour", Toast.LENGTH_SHORT).show();
                            } else {
                                // URL de l'API qui récupère les données des notes de frais
                                String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense/create?expenseTotalB=" + String.valueOf(amount.getText()) + "&businessExpenseLabel=" + category + "&businessExpenseDetails=" + details.getText() + "&businessExpenseDate=" + dateExpense.getText() + "&expenseReportCodeB=" + erCode+"&urlProof="+urlProof+"&titleProof=justificatif_"+String.valueOf(System.currentTimeMillis());
                                //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/businessexpense/create?expenseTotalB=" + String.valueOf(amount.getText()) + "&businessExpenseLabel=" + category + "&businessExpenseDetails=" + details.getText() + "&businessExpenseDate=" + dateExpense.getText() + "&expenseReportCodeB=" + erCode + "&urlProof=" + urlProof + "&titleProof=justificatif_" + String.valueOf(System.currentTimeMillis());
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
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlProof = "justificatif_" +String.valueOf(System.currentTimeMillis()) + ".jpg";
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
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);
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
}
