package ru.mirea.borodkinada.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showToast(View view) {
        EditText editText = findViewById(R.id.editText);
        String inputText = editText.getText().toString();
        int charCount = inputText.length();

        String message = "СТУДЕНТ № 5 ГРУППА БСБО-11-21> Количество символов - " + charCount;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}