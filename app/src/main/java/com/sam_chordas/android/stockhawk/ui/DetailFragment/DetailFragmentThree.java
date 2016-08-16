package com.sam_chordas.android.stockhawk.ui.DetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragmentThree extends Fragment{

    private GraphView graphView;
    private BarGraphSeries barGraphSeries;
    private ArrayList<String> stockCloseValueList;

    public DetailFragmentThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_fragment_three, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(stockCloseValueList != null){
            outState.putStringArrayList("stockCloseValueList",stockCloseValueList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        graphView = (GraphView) getView().findViewById(R.id.bar_graph);
        if(savedInstanceState != null && savedInstanceState.getStringArrayList("stockCloseValueList") != null){
            setDetailGraphView(savedInstanceState.getStringArrayList("stockCloseValueList"));
        }
    }

    public void setDetailGraphView(ArrayList<String> stockCloseValueList){
        this.stockCloseValueList = stockCloseValueList;
        barGraphSeries = new BarGraphSeries();
        for(int i = 0; i<stockCloseValueList.size(); i++){
            barGraphSeries.appendData(new DataPoint(i, Double.parseDouble(stockCloseValueList.get(i))), false, 366);
        }

        graphView.addSeries(barGraphSeries);

        Viewport viewport = graphView.getViewport();
        viewport.setMinY(0);
        viewport.setMaxY(1000);
        viewport.setScrollable(true);
        viewport.setScalable(true);
    }
}
