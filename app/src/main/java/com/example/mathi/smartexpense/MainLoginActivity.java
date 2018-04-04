package com.example.mathi.smartexpense;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainLoginActivity extends AppCompatActivity {

    private EditText inputLogin;
    private EditText inputPassword;
    private Button connexionButton;
    private TextView forgotPasswordLink;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }
}
