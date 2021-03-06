package com.eddystudio.bartbetter.ViewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.view.View;

import com.eddystudio.bartbetter.Model.Response.EstimateResponse.Etd;
import com.eddystudio.bartbetter.Model.Uilt;

public class QuickLookupRecyclerViewItemVM extends ViewModel {
  public String from = "";

  public final MutableLiveData<String> destination = new MutableLiveData<>();
  public final MutableLiveData<String> firstTrain = new MutableLiveData<>();
  public final MutableLiveData<String> secondTrain = new MutableLiveData<>();
  public final MutableLiveData<String> thirdTrain = new MutableLiveData<>();
  public final MutableLiveData<Integer> routColor = new MutableLiveData<>();
  public final MutableLiveData<String> routeCarNumber = new MutableLiveData<>();
  private final Etd etd;

  private ItemClickListener itemClickListener;

  public QuickLookupRecyclerViewItemVM(String from, Etd etd) {
    this.from = from;
    this.etd = etd;
    updateUi();
  }

  public void setItemClickListener(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  public void onItemClicked(View view) {
    String to = "";
    if(destination != null) {
      to = etd.getAbbreviation();
    }
    itemClickListener.onItemClicked(from, to, routColor.getValue(), view);
  }

  private void updateUi() {
    String first = "";
    String second = "";
    String third = "";
    if(etd.getEstimate().size() == 1) {
      first = etd.getEstimate().get(0).getMinutes().equals("Leaving") ? "0" : etd.getEstimate().get(0).getMinutes();
    } else if(etd.getEstimate().size() == 2) {
      first = etd.getEstimate().get(0).getMinutes().equals("Leaving") ? "0" : etd.getEstimate().get(0).getMinutes();
      second = etd.getEstimate().get(1).getMinutes();
    } else {
      first = etd.getEstimate().get(0).getMinutes().equals("Leaving") ? "0" : etd.getEstimate().get(0).getMinutes();
      second = etd.getEstimate().get(1).getMinutes();
      third = etd.getEstimate().get(2).getMinutes();
    }

    this.destination.setValue(etd.getDestination());
    this.firstTrain.setValue(first);
    this.secondTrain.setValue(second.equals("") ? "" : ", " + second);
    this.thirdTrain.setValue(third.equals("") ? "" : ", " + third);
    this.routColor.setValue(Uilt.materialColorConverter(etd.getEstimate().get(0).getColor()));
    this.routeCarNumber.setValue(etd.getEstimate().get(0).getLength());
  }

}
