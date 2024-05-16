package ru.mirea.borodkinada.lesson4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import ru.mirea.borodkinada.lesson4.databinding.ActivityMusicBinding;

public class MusicActivity extends AppCompatActivity {
    private ActivityMusicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playButton.setOnClickListener(v -> Log.d("MusicActivity", "Play button clicked"));
        binding.previousButton.setOnClickListener(v -> Log.d("MusicActivity", "Previous button clicked"));
        binding.nextButton.setOnClickListener(v -> Log.d("MusicActivity", "Next button clicked"));
        binding.favoriteButton.setOnClickListener(v -> Log.d("MusicActivity", "Favorite button clicked"));
        binding.moreActions.setOnClickListener(v -> Log.d("MusicActivity", "More actions clicked"));
    }
}