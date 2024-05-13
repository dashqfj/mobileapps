package ru.mirea.borodkinada.sharer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void openSendActivity(View view) {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    // Метод для открытия PickActivity
    public void openPickActivity(View view) {
        Intent intent = new Intent(this, PickActivity.class);
        startActivity(intent);
    }
}