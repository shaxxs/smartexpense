package com.example.mathi.smartexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RefundTrackerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_tracker);


        /* Gestion du clic sur le bouton retour */
        Button bouton = findViewById(R.id.returnButton);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* Lien vers la vue Tableau de bord */
                Intent intent = new Intent(RefundTrackerActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
