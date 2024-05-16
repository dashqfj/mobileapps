package ru.mirea.borodkinada.mireaproject;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;


import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkFragment extends Fragment {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    private TextView nameTextView;
    private TextView heightTextView;
    private TextView weightTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_network, container, false);

        nameTextView = rootView.findViewById(R.id.text_name);
        heightTextView = rootView.findViewById(R.id.text_height);
        weightTextView = rootView.findViewById(R.id.text_weight);

        fetchData();

        return rootView;
    }

    private static final String[] POKEMON_NAMES = {"pikachu", "bulbasaur", "charmander", "squirtle", "eevee"};

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Выбираем случайное имя покемона из массива
        Random random = new Random();
        String randomPokemonName = POKEMON_NAMES[random.nextInt(POKEMON_NAMES.length)];

        // Получить информацию о случайном покемоне
        Call<DataModel> call = apiService.getPokemonDetails(randomPokemonName);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                if (response.isSuccessful()) {
                    DataModel data = response.body();
                    if (data != null) {
                        nameTextView.setText("Name: " + data.getName());
                        heightTextView.setText("Height: " + data.getHeight());
                        weightTextView.setText("Weight: " + data.getWeight());
                    } else {
                        Log.e("NetworkFragment", "Response body is null");
                    }
                } else {
                    Log.e("NetworkFragment", "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                Log.e("NetworkFragment", "Request failed: " + t.getMessage());
            }
        });
    }
}
