package com.Guadarrama.mapas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

  private MapView map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EdgeToEdge.enable(this);
    Configuration.getInstance().setUserAgentValue(getPackageName());

    setContentView(R.layout.activity_main);

    map = findViewById(R.id.map);
    map.setMultiTouchControls(true);

    // Punto inicial: Ciudad de México
    GeoPoint startPoint = new GeoPoint(19.4326, -99.1332);
    map.getController().setZoom(12.6);
    map.getController().setCenter(startPoint);

    // Marcador en CDMX
    Marker marker = new Marker(map);
    marker.setPosition(startPoint);
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    marker.setTitle("CDMX");

    // Agregar marcador al mapa
    map.getOverlays().add(marker);
  }
}

