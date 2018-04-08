package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExpenseReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);


/* Gestion du clic sur le bouton retour */
        Button bouton = findViewById(R.id.returnButton);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intent = new Intent(ExpenseReportActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
