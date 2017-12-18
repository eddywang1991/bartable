package com.example.eddystudio.bartable.Repository;


import com.example.eddystudio.bartable.Repository.Response.EstimateResponse.Bart;
import com.example.eddystudio.bartable.Repository.Response.Stations.BartStations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BartService {
    @GET("api/etd.aspx")
    Call<Bart> bartEstmate(@Query("cmd") String fromStation, @Query("orig") String origin, @Query("key") String key, @Query("json") String isJason);

    @GET("api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V&json=y")
    Call<BartStations> bartStations();
}
