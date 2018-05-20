package com.eddystudio.bartbetter.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eddystudio.bartbetter.Adapter.RouteDetailRecyclerViewAdapter;
import com.eddystudio.bartbetter.Model.Repository;
import com.eddystudio.bartbetter.Model.Response.Schedule.ScheduleFromAToB;
import com.eddystudio.bartbetter.Model.Response.Schedule.Trip;
import com.eddystudio.bartbetter.Model.Uilt;
import com.eddystudio.bartbetter.ViewModel.RouteDetailRecyclerViewModel;
import com.eddystudio.bartbetter.ViewModel.RouteDetailViewModel;
import com.eddystudio.bartbetter.R;

import java.util.ArrayList;
import java.util.List;

import com.eddystudio.bartbetter.databinding.FragmentRoutDetailBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RouteDetailFragment extends Fragment {
    private FragmentRoutDetailBinding binding;
    private String from;
    private String to;
    private int color;
    private RouteDetailRecyclerViewAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public Repository repository = new Repository();
    private AppBarLayout appBarLayout;

    public RouteDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoutDetailBinding.inflate(inflater, container, false);

        ImageView imageView = getActivity().findViewById(R.id.toolbar_imageView);
        CollapsingToolbarLayout collapsingToolbarLayout = getActivity().findViewById(R.id.toolbar_layout);
        appBarLayout = getActivity().findViewById(R.id.app_bar);
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle arg = getArguments();
        if (arg != null) {
            from = arg.getString(MainActivity.BUDDLE_ARG_FROM);
            to = arg.getString(MainActivity.BUDDLE_ARG_TO);
            color = arg.getInt("color");
        }
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        binding.setVm(new RouteDetailViewModel(from, to, color));


        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(Uilt.randomCityBgGenerator());
        collapsingToolbarLayout.setTitleEnabled(true);
        appBarLayout.setExpanded(true, true);
        collapsingToolbarLayout.setTitle(Uilt.getFullStationName(to));
        collapsingToolbarLayout.setPadding(0, 0, 0, 0);
        setupAdapter();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 800));
        getRoutesInfo();
        binding.swipeRefreshLy.setOnRefreshListener(this::getRoutesInfo);

    }

    private void getRoutesInfo() {
        List<Pair<String, String>> routes = new ArrayList<>();
        routes.add(new Pair<>(from, to));
        adapter.clearAllData();
        compositeDisposable.add(
                repository.getRouteSchedules(routes)
                        .doOnSubscribe(ignored -> binding.swipeRefreshLy.setRefreshing(true))
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(this::getTrips)
                        .subscribe(this::addToAdapter, this::handleError, this::onComplete)
        );
    }

    private void addToAdapter(List<Trip> trips) {
        for (Trip trip : trips) {
            RouteDetailRecyclerViewModel vm = new RouteDetailRecyclerViewModel(trip);
            adapter.addData(vm);
        }
    }

    private List<Trip> getTrips(ScheduleFromAToB schedule) {
        return schedule.getRoot().getSchedule().getRequest().getTrip();
    }

    private void handleError(Throwable throwable) {
    }

    private void onComplete() {
        binding.swipeRefreshLy.setRefreshing(false);
    }

    private void setupAdapter() {
        ArrayList<RouteDetailRecyclerViewModel> routeInfoList = new ArrayList<>();
        adapter = new RouteDetailRecyclerViewAdapter(routeInfoList, binding.routeDetailRecyclerview.getId(),
                R.layout.route_detail_single_recycler_view_item);
        binding.routeDetailRecyclerview.setAdapter(adapter);
        binding.routeDetailRecyclerview.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.routeDetailRecyclerview.setLayoutManager(manager);
        binding.pageIndicator.attachToRecyclerView(binding.routeDetailRecyclerview);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.routeDetailRecyclerview);
    }

    @Override
    public void onStop() {
        super.onStop();
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        compositeDisposable.clear();
    }
}
