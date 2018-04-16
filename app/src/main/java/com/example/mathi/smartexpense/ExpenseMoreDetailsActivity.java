package com.example.mathi.smartexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExpenseMoreDetailsActivity extends AppCompatActivity {

    final String EXPENSE_ID = "expense_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_more_details);

        final Intent intent1 = getIntent();

/* Gestion du clic sur le bouton Retour */
        Button button1 = (Button) findViewById(R.id.returnButtonExpenseMoreDetails);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Lien vers la vue Dépense - Détails */
                Intent intent = new Intent(ExpenseMoreDetailsActivity.this, ExpenseDetailsActivity.class);
                startActivity(intent);
            }
        });

/* Déclaration des TextView de la vue */
        TextView departureDate = (TextView) findViewById(R.id.departureDate);
        TextView returnDate = (TextView) findViewById(R.id.expenseReturnDate);
        TextView departureCity = (TextView) findViewById(R.id.expenseDepartureCity);
        TextView validationDate = (TextView) findViewById(R.id.expenseValidationDate);
        TextView totalAmount = (TextView) findViewById(R.id.expenseTotalAmount);
        TextView refundAmount = (TextView) findViewById(R.id.expenseRefundAmount);
        TextView paymentDate = (TextView) findViewById(R.id.expensePaymentDate);
    }
}
