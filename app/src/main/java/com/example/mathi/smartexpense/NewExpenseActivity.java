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
import java.util.Calendar;
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
    final String EXPENSE_REPORT_CODE = "expense_report_code";
    final String NEW_EXPENSE_REPORT = "new_expense_report";
    final String FILE_EXPENSE_REPORT = "file_expense_report";
    SharedPreferences sharedPreferencesER;
    private int erCode;

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

/** GESTION DU CHOIX DE LA DATE **/

        //initialiser le champs date
        dateExpense = findViewById(R.id.dateExpense);
        // perform click event on edit text
        dateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                Locale.setDefault(Locale.FRANCE);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateExpense.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //initialiser le champs date
        dateDeparture = findViewById(R.id.dateDeparture);
        // perform click event on edit text
        dateDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                Locale.setDefault(Locale.FRANCE);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateDeparture.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //initialiser le champs date
        dateArrival = findViewById(R.id.dateArrival);
        // perform click event on edit text
        dateArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                Locale.setDefault(Locale.FRANCE);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewExpenseActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateArrival.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//Gestion du clic sur le bouton Valider
        final Button buttonValid = (Button) findViewById(R.id.validButton);
        sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);

        if (sharedPreferencesER.contains(EXPENSE_REPORT_CODE)) {
            erCode = sharedPreferencesER.getInt(EXPENSE_REPORT_CODE, 0);
        }
        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enregistrement de la nouvelle dépense dans la db
                //Si c'est un trajet
                if (category.equals("Trajet")) {
                    if (String.valueOf(amount.getText()).equals("") || departureCity.getText().equals("") || arrivalCity.getText().equals("") || dateDeparture.getText().equals("") || dateArrival.getText().equals("") || String.valueOf(kms.getText()).equals("")) {
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires",Toast.LENGTH_SHORT).show();
                    } else {

                        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity.getText()+"&destinationCity="+arrivalCity.getText()+"&departureDate="+dateDeparture.getText()+"&returnDate="+dateArrival.getText()+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+ExpenseReportCodeTemporaire;
                        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/travel/create?expenseTotalT="+String.valueOf(amount.getText())+"&travelDuration="+String.valueOf(durationTravel.getText())+"&departureCity="+departureCity.getText()+"&destinationCity="+arrivalCity.getText()+"&departureDate="+dateDeparture.getText()+"&returnDate="+dateArrival.getText()+"&km="+String.valueOf(kms.getText())+"&expenseReportCodeT="+erCode;

                        HttpGetRequest getRequest = new HttpGetRequest();
                        String result = "";
                        try {
                            result = getRequest.execute(myURL).get();
                            System.out.println("Retour HTTPGetRequest : " + result);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("Succes")) {
                            Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée",Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Si c'est un businessexpense
                } else {
                    if (String.valueOf(amount.getText()).equals("") || dateExpense.getText().equals("")) {
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner tous les champs obligatoires",Toast.LENGTH_SHORT).show();
                    } else {

                        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/businessexpense/create?expenseTotalB="+String.valueOf(amount.getText())+"&businessExpenseDetails="+details.getText()+"&businessExpenseDate="+dateExpense.getText()+"&expenseReportCodeB="+ExpenseReportCodeTemporaire;
                        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/businessexpense/create?expenseTotalB=" + String.valueOf(amount.getText()) + "&businessExpenseLabel=" + category + "&businessExpenseDetails=" + details.getText() + "&businessExpenseDate=" + dateExpense.getText() + "&expenseReportCodeB="+erCode;
                        
                        HttpGetRequest getRequest = new HttpGetRequest();
                        String result = "";
                        try {
                            result = getRequest.execute(myURL).get();
                            System.out.println("Retour HTTPGetRequest : " + result);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("Succes")) {
                            Toast.makeText(getApplicationContext(), "Nouvelle dépense ajoutée", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //Lien vers la vue Note de frais
                Intent intentValid = new Intent(NewExpenseActivity.this, ERDetailsActivity.class);
                startActivity(intentValid);
            }
        });


//Gestion du clic sur le bouton Ajouter un justificatif
        buttonProof = findViewById(R.id.btnAddProof);
        Log.v("Path image", this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath());
        buttonProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
// Nous n'avons pas la permission de stockée dans le
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

        //Gestion du clic sur le bouton Retour
        Button buttonReturn = (Button) findViewById(R.id.returnButton);
        buttonReturn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                // on récupère les données de notre fichier SharedPreferences
                sharedPreferencesER = getSharedPreferences(FILE_EXPENSE_REPORT, MODE_PRIVATE);
                Boolean newExpenseReport = false;
                // si on vient de la page nouvelle note de frais, renvoi vers la liste des notes de frais
                // si on vient de la page note de frais - details, renvoi vers celle ci
                if (sharedPreferencesER.contains(NEW_EXPENSE_REPORT)) {
                    newExpenseReport = sharedPreferencesER.getBoolean(NEW_EXPENSE_REPORT, false);
                }
                if (newExpenseReport.equals(false)) {
                    //Lien vers la vue Note de Frais Détails
                    Intent intentReturn = new Intent(NewExpenseActivity.this, ERDetailsActivity.class);
                    startActivity(intentReturn);
                } else {
                    //Lien vers la vue Note de frais
                    Intent intentReturn = new Intent(NewExpenseActivity.this, ExpenseReportActivity.class);
                    startActivity(intentReturn);
                }
            }
        });

        //Gestion de la liste déroulante
        addListenerOnSpinnerItemSelection();

    }
//traitement de la prise de vue par l'appareil photo en l'appelant et en enregistrant la photo dans le lien envoyé via putextra
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

    //lorsque la photo a été prise et la photo enregistré on peut appeler la photo pour la mettre dans une image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                retrieveCapturedPicture();
            }
        }
    }

    //pour traiter la photo qui a été prise pour le justificatif en l'envoyant vers l'image view mImageThumbnail
    private void retrieveCapturedPicture() {
        // display picture on ImageView
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        mImageThumbnail.setImageBitmap(bitmap);
        mImageThumbnail.setVisibility(View.VISIBLE);
    }

    //Gestion de la liste déroulante
    public void addListenerOnSpinnerItemSelection() {
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(this);
    }

    //Gestion de l'item sélectionné dans la liste déroulante et des champs affichés sur la vue
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        category = parent.getItemAtPosition(pos).toString();
        if (category.equals("Trajet")) {
            departureCity.setVisibility(View.VISIBLE);
            dateExpense.setVisibility(View.GONE);
            dateDeparture.setVisibility(View.VISIBLE);
            dateArrival.setVisibility(View.VISIBLE);
            arrivalCity.setVisibility(View.VISIBLE);
            kms.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
            durationTravel.setVisibility(View.VISIBLE);
            departureCity.setVisibility(View.VISIBLE);
        } else {
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
}
