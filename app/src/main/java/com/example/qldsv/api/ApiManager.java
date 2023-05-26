package com.example.qldsv.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final  String BASE_URL ="https://api-qldsv.vercel.app/";
    private static  ApiService apiService;
    private static ApiManager instance;

    private ApiManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if(instance == null){
            instance = new ApiManager();
        }
        return instance;
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
