package com.example.eddystudio.bartable.Dashboard;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.eddystudio.bartable.R;
import com.example.eddystudio.bartable.Repository.Repository;
import com.example.eddystudio.bartable.Repository.Response.EstimateResponse.Bart;
import com.example.eddystudio.bartable.Repository.Response.EstimateResponse.Etd;
import com.example.eddystudio.bartable.Uilts.BaseRecyclerViewAdapter;
import com.example.eddystudio.bartable.Uilts.CardSwipeController;
import com.example.eddystudio.bartable.Uilts.SwipeControllerActions;
import com.example.eddystudio.bartable.application.Application;
import com.example.eddystudio.bartable.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.example.eddystudio.bartable.MainActivity.DASHBOARDROUTS;
import static com.example.eddystudio.bartable.MainActivity.dashboardRouts;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private final Repository repository = new Repository();
    private String originStation;
    private final ArrayList<DashboardRecyclerViewItemModel> dashboardVmList = new ArrayList<>();


    @Inject
    public SharedPreferences preference;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) binding.appToolbar);
        binding.appToolbar.setTitle("Dashboard");

        Application.getAppComponet().inject(this);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFromPrerence();
        binding.swipeRefreshLy.setOnRefreshListener(this::loadFromPrerence);
        attachOnCardSwipe();
    }

    private void attachOnCardSwipe() {
        CardSwipeController cardSwipeController = new CardSwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
                ArrayList<String> arrayList = new ArrayList<>(dashboardRouts);
                arrayList.remove(position);
                SharedPreferences.Editor editor = preference.edit();
                editor.putStringSet(DASHBOARDROUTS,  new HashSet<>(arrayList));
                editor.apply();
                Snackbar.make(binding.recylerView,"Removed", Snackbar.LENGTH_LONG).show();
                loadFromPrerence();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(cardSwipeController);
        itemTouchHelper.attachToRecyclerView(binding.recylerView);
        binding.recylerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                cardSwipeController.onDraw(c,"Remove","#D32F2F");
            }
        });
    }

    private void loadFromPrerence(){
        Set<String> dashboardRouts;
        dashboardRouts = preference.getStringSet(DASHBOARDROUTS, new HashSet<>());
        ArrayList<String> list = new ArrayList<>(dashboardRouts);
        Log.d("dashboard", list.toString());
        dashboardVmList.clear();
        for (int i = 0; i < list.size(); ++i){
            String from = list.get(i).split("-",2)[0];
            String to = list.get(i).split("-",2)[1];
            Log.d("dashboard","From " + from + " to " + to);
            init(from, to);
        }
    }

    private void init(String fromStation, String toStation) {

        repository.getEstimate(fromStation)
                .doOnSubscribe(ignored -> binding.swipeRefreshLy.setRefreshing(true))
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::getEtd)
                .concatMap(Observable::fromArray)
                .map(etds -> convertToVM(etds, toStation))
                .doOnNext(data -> {
                    int resId = R.anim.layout_animation_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    binding.recylerView.setLayoutAnimation(animation);
                    BaseRecyclerViewAdapter adapters = new DashboardRecyclerViewAdapter(data,binding.recylerView.getId(), R.layout.dashboard_single_recycler_view_item);
                    binding.recylerView.setAdapter(adapters);
                    binding.recylerView.setNestedScrollingEnabled(false);
                    binding.recylerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                })
                .doOnError(this::handleError)
                .doOnComplete(() -> binding.swipeRefreshLy.setRefreshing(false))
                .subscribe();
    }

    private void handleError(Throwable throwable) {
        Log.e("error", "error on getting response", throwable);
        Snackbar.make(binding.recylerView, "error on loading", Snackbar.LENGTH_LONG).show();
    }

    private ArrayList<DashboardRecyclerViewItemModel> convertToVM(List<Etd> etd, String toStation) {
        for (int i = 0; i < etd.size(); ++i){
            if (etd.get(i).getAbbreviation().equals(toStation)){
                dashboardVmList.add(new DashboardRecyclerViewItemModel(etd.get(i),originStation));
                break;
            }
        }

        return dashboardVmList;
    }

    private List<Etd> getEtd(Bart bart) {
        originStation = bart.getRoot().getStation().get(0).getName();
        return bart.getRoot().getStation().get(0).getEtd();
    }
}
