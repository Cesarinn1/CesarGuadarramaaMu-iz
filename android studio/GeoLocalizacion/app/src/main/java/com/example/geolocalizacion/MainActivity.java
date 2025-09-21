package com.Guadarrama.geolocalizacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

  private TextView txtLocation;
  private static final int REQUEST_CODE_LOCATION = 1;
  private FusedLocationProviderClient fusedLocationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    txtLocation = findViewById(R.id.txtLabel);

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    // Verificamos permisos
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
        this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        REQUEST_CODE_LOCATION
      );
    } else {
      getLocation();
    }
  }

  private void getLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED) {
      fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, location -> {
          if (location != null) {
            txtLocation.setText(
              "Latitude: " + location.getLatitude() +
                "\nLongitude: " + location.getLongitude()
            );
          } else {
            txtLocation.setText("No se pudo obtener la ubicación.");
          }
        });
    }
  }

  // Manejo de permisos
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == REQUEST_CODE_LOCATION) {
      if (grantResults.length > 0 &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLocation(); // Usuario dio permiso
      } else {
        txtLocation.setText("Permiso de ubicación denegado.");
      }
    }
  }
}
