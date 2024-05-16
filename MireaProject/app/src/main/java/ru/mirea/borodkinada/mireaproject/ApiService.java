package ru.mirea.borodkinada.mireaproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("pokemon/{name}/")
    Call<DataModel> getPokemonDetails(@Path("name") String name);
}
