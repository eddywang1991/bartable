package com.example.eddystudio.bartable.Model;

import android.util.Pair;
import com.example.eddystudio.bartable.Model.Response.DelayReport.DelayReport;
import com.example.eddystudio.bartable.Model.Response.ElevatorStatus.ElevatorStatus;
import com.example.eddystudio.bartable.Model.Response.EstimateResponse.Bart;
import com.example.eddystudio.bartable.Model.Response.Schedule.ScheduleFromAToB;
import com.example.eddystudio.bartable.Model.Response.Stations.BartStations;
import com.example.eddystudio.bartable.DI.Application;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Repository {
  private BartService bartService;
  private static final String KEY = "MW9S-E7SL-26DU-VV8V";

  @Inject
  Retrofit retrofit;

  @Inject
  public Repository() {
    Application.getAppComponet().inject(this);
    bartService = retrofit.create(BartService.class);
  }

  //?cmd=etd&orig={fromStation}&key=MW9S-E7SL-26DU-VV8V&json=y"
  public io.reactivex.Observable<Pair<Bart, String>> getEstimate(
      List<Pair<String, String>> fromStation) {
    return io.reactivex.Observable
        .fromIterable(fromStation)
        .flatMap(pair ->
            Observable.fromCallable(
                () -> bartService.bartEstmate("etd", pair.first, KEY, "y")
                    .execute())
                .map(Response::body)
                .map(bart -> new Pair<>(bart, pair.second))
                .onErrorResumeNext(Observable.empty())
        )
        .subscribeOn(Schedulers.io());
  }

  public Observable<BartStations> getStations() {
    return Observable.fromCallable(() -> bartService.bartStations().execute())
        .map(Response::body)
        .subscribeOn(Schedulers.io());
  }

  public Observable<DelayReport> getDelayReport() {
    return Observable.fromCallable(() -> bartService.delayReport().execute())
        .map(Response::body)
        .subscribeOn(Schedulers.io());
  }

  public Observable<ElevatorStatus> getElevatorStatus() {
    return Observable.fromCallable(() -> bartService.elevatorStatus().execute())
        .map(Response::body)
        .subscribeOn(Schedulers.io());
  }

  //https://api.bart.gov/api/sched.aspx? cmd=depart  &orig=DALY&  dest=FRMT&  date=now&  key=MW9S-E7SL-26DU-VV8V&  b=0  &a=3  &l=1&  json=y
  public Observable<ScheduleFromAToB> getRouteSchedules(String from, String to) {
    return Observable.fromCallable(
        () -> bartService.routeSchedules("depart", from, to, "now", KEY, "0", "3", "1", "y")
            .execute())
        .map(Response::body)
        .subscribeOn(Schedulers.io());
  }
}
