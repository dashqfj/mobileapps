package ru.mirea.borodkinada.timeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.mirea.borodkinada.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String host = "time.nist.gov"; // или time-a.nist.gov
    private final int port = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            String[] dateAndTime = new String[2];
            try {
                java.net.Socket socket = new java.net.Socket(host, port);
                BufferedReader reader = Socket.getReader(socket); // Используем метод getReader из класса Socket
                reader.readLine(); // игнорируем первую строку
                String dateTimeString = reader.readLine(); // считываем вторую строку
                Log.d("Socket", dateTimeString);
                socket.close();
                // Разделение строки на дату и время
                String[] parts = dateTimeString.split("\\s+"); // Разделение по пробелам
                dateAndTime[0] = parts[1]; // Дата
                dateAndTime[1] = parts[2]; // Время
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dateAndTime;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            // Вывод даты и времени в соответствующие текстовые поля
            binding.dateTextView.setText(result[0]);
            binding.timeTextView.setText(result[1]);
        }
    }

}
