package com.example.gilado.senso.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gilado.senso.R;
import com.example.gilado.senso.main.IMain.IMainView;
import com.example.gilado.senso.main.model.sensor.BaseSensor;
import com.example.gilado.senso.main.view.adapter.SensorListAdapter;

import java.util.List;

/**
 * Created by gilado on 9/23/2017.
 */

public class MainView extends Fragment implements IMainView {

    private IMainViewListener mMainPresenter;
    private RecyclerView      mRecyclerView;
    private SensorListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_view_fragment, container, false);
        setUpUi(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainPresenter.onViewResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMainPresenter.onViewPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMainPresenter.onViewDestroy();
    }

    @Override
    public void setPresenter(IMainViewListener presenter) {
        mMainPresenter = presenter;
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void showSensorList(List<BaseSensor> sensors) {
        adapter.setList(sensors);
    }

    @Override
    public void onSelectedSensorLoad(List<BaseSensor> baseSensors) {

    }

    private void setUpUi(View view) {
        mRecyclerView = view.findViewById(R.id.sensors);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        adapter = new SensorListAdapter();
        mRecyclerView.setAdapter(adapter);
    }
}