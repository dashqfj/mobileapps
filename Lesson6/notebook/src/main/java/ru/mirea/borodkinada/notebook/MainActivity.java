package ru.mirea.borodkinada.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.borodkinada.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    public void SaveData(View view){
        if (isExternalStorageWritable()) {
            String fileName = binding.editTextFileName.getText().toString();
            String citata = binding.editTextCitata.getText().toString();

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, fileName +".txt");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
                OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);

                output.write(citata);
                output.close();
                Toast.makeText(MainActivity.this, "Цитата добавлена в /storage/emulated/0/Documents", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void LoadData(View view){
        if (isExternalStorageReadable()){
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, binding.editTextFileName.getText().toString() + ".txt");
            try	{
                FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                List<String> lines = new ArrayList<String>();
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
                binding.editTextCitata.setText(lines.get(0).toString());
                Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show();
            }	catch	(Exception	e)	{
                Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}