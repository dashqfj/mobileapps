package ru.mirea.borodkinada.osmmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка конфигурации карты
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("OpenStreetMapPrefs", MODE_PRIVATE));

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Запрос разрешений на доступ к местоположению
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Разрешение уже предоставлено, добавляем слой местоположения на карту
            addMyLocationOverlay();
        }

        // Добавление маркеров
        addMarkers();

        // Установка начальной позиции и зума после добавления слоя местоположения и маркеров
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(55.794229, 37.700772)); // Установка начальной позиции

        // Добавление компаса
        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // Отображение метрической шкалы масштаба
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        mapView.getOverlays().add(scaleBarOverlay);

        // Перерисовываем карту
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // Метод для добавления слоя местоположения на карту
    private void addMyLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    // Обработка результата запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, добавляем слой местоположения на карту
                addMyLocationOverlay();
            }
        }
    }

    // Метод для добавления маркеров
    private void addMarkers() {
        // Создание маркера с описанием
        Marker marker1 = new Marker(mapView);
        marker1.setPosition(new GeoPoint(55.715094, 37.544187));
        marker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker1.setTitle("Воробьевы горы");
        marker1.setSnippet("Смотровая площадка");
        mapView.getOverlays().add(marker1);

        // Создание и добавление других маркеров по аналогии
        Marker marker2 = new Marker(mapView);
        marker2.setPosition(new GeoPoint(55.716336, 37.554466));
        marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker2.setTitle("Лужники");
        marker2.setSnippet("Спортивный комплекс");
        mapView.getOverlays().add(marker2);

    }
}
