package com.Guadarrama.calculadoraimc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  EditText d_cuenta, d_personas;
  Button calcular;
  TextView resultado;
  RadioGroup opcionesPropina;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    d_cuenta = findViewById(R.id.cuenta);
    d_personas = findViewById(R.id.personas);
    calcular = findViewById(R.id.calcular);
    resultado = findViewById(R.id.res);
    opcionesPropina = findViewById(R.id.opcionesPropina);

    calcular.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        double cuenta = Double.parseDouble(d_cuenta.getText().toString());
        int personas = Integer.parseInt(d_personas.getText().toString());

        // Obtener propina seleccionada
        int selectedId = opcionesPropina.getCheckedRadioButtonId();
        double porcentaje = 0;

        if (selectedId == R.id.op5) {
          porcentaje = 0.05;
        } else if (selectedId == R.id.op10) {
          porcentaje = 0.10;
        } else if (selectedId == R.id.op15) {
          porcentaje = 0.15;
        }

        double totalConPropina = cuenta + (cuenta * porcentaje);
        double porPersona = totalConPropina / personas;

        resultado.setText("Total con propina: $" + totalConPropina +
          "\nCada persona paga: $" + String.format("%.2f", porPersona));
      }
    });
  }
}
