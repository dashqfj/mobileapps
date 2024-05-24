package ru.mirea.borodkinada.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

public class PlacesFragment extends Fragment {

    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        // Настройка конфигурации карты
        Configuration.getInstance().load(requireContext(),
                requireActivity().getSharedPreferences("OpenStreetMapPrefs", getActivity().MODE_PRIVATE));

        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Запрос разрешений на доступ к местоположению
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
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
        CompassOverlay compassOverlay = new CompassOverlay(requireContext(), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // Отображение метрической шкалы масштаба
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        mapView.getOverlays().add(scaleBarOverlay);

        // Перерисовываем карту
        mapView.invalidate();

        // Добавление обработчика касаний
        mapView.setOnTouchListener(new View.OnTouchListener() {
            private long lastTouchDown;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastTouchDown = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    long touchDuration = System.currentTimeMillis() - lastTouchDown;
                    if (touchDuration > 500) { // Считаем долгим нажатием, если длительность больше 500 мс
                        GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                        addMarker(geoPoint);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // Метод для добавления слоя местоположения на карту
    private void addMyLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
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
        Marker marker1 = new Marker(mapView);
        marker1.setPosition(new GeoPoint(55.772181, 37.604685));
        marker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker1.setTitle("улица Малая Дмитровка 20");
        marker1.setSnippet("ресторан Бутчер");
        mapView.getOverlays().add(marker1);

        Marker marker2 = new Marker(mapView);
        marker2.setPosition(new GeoPoint(55.778404, 37.586527));
        marker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker2.setTitle("Лесная улица 5");
        marker2.setSnippet("мясной ресторан Torro Grill");
        mapView.getOverlays().add(marker2);

        Marker marker3 = new Marker(mapView);
        marker3.setPosition(new GeoPoint(55.757363, 37.606109));
        marker3.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker3.setTitle("Большая Никитская улица 14");
        marker3.setSnippet("Бар");
        mapView.getOverlays().add(marker3);
    }

    // Метод для добавления маркера на карте по длинному нажатию
    private void addMarker(GeoPoint geoPoint) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle("Новая метка");
        marker.setSnippet("Долгое нажатие, чтобы добавить");
        mapView.getOverlays().add(marker);
        mapView.invalidate(); // Перерисовываем карту
        Toast.makeText(getContext(), "Метка добавлена: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}
