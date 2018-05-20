package com.eddystudio.bartbetter.ViewModel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.eddystudio.bartbetter.Model.Uilt;

public class RouteDetailViewModel {
  public ObservableField<String> from = new ObservableField<>("");
  public ObservableField<String> to = new ObservableField<>("");
  public ObservableInt color = new ObservableInt();

  public RouteDetailViewModel(String from, String to, int color) {
    this.from.set(Uilt.getFullStationName(from));
    this.to.set(Uilt.getFullStationName(to));
    this.color.set(color);
  }
}
