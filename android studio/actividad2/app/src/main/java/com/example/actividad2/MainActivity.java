package com.Guadarrama.actividad2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

  EditText etUserName, etPassword;
  Button btnLogin, btnCancel;
  TextView tvMessage;

  String[][] credenciales = {
    
    {"Guadarrama", "abcd"},
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etUserName = findViewById(R.id.etUserName);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    btnCancel = findViewById(R.id.btnCancel);
    tvMessage = findViewById(R.id.tvMessage);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String user = etUserName.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        boolean acceso = false;

        // Verificar si las credenciales coinciden con alguna en la lista
        for (String[] cred : credenciales) {
          if (user.equalsIgnoreCase(cred[0]) && pass.equals(cred[1])) {
            acceso = true;
            break;
          }
        }

        if (acceso) {
          tvMessage.setText("ACCESO CORRECTO");
          tvMessage.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
        } else {
          tvMessage.setText("ACCESO DENEGADO");
          tvMessage.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark));
        }
      }
    });

    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        etUserName.setText("");
        etPassword.setText("");
        tvMessage.setText("");
      }
    });
  }
}
