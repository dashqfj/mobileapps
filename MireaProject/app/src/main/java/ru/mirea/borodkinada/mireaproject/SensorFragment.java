package ru.mirea.borodkinada.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {

    private static final int REQUEST_ACCELEROMETER_PERMISSION = 1;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private TextView textViewDirection;

    public SensorFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        initializeViews(view);
        checkAndRequestAccelerometerPermission();
        return view;
    }

    private void initializeViews(View view) {
        textViewDirection = view.findViewById(R.id.textView_direction);
    }


    private void checkAndRequestAccelerometerPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BODY_SENSORS}, REQUEST_ACCELEROMETER_PERMISSION);
        } else {
            registerAccelerometerListener();
        }
    }

    private void registerAccelerometerListener() {
        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensorAccelerometer != null) {
            sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(getContext(), "Датчик акселерометра недоступен", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCELEROMETER_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerAccelerometerListener();
            } else {
                Toast.makeText(getContext(), "Доступ к акселерометру отклонен", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (z > 9.0) {
            textViewDirection.setText("Устройство Лежит");
        } else if (y > 9.0) {
            textViewDirection.setText("Устройство Стоит");
        } else if (y < -9.0) {
            textViewDirection.setText("Устройство перевернуто");
        } else {
            textViewDirection.setText("Устройство находится в руке пользователя");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
