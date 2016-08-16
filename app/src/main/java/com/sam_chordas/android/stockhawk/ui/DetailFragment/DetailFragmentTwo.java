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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragmentTwo extends Fragment{

    private GraphView graphView;
    private PointsGraphSeries pointsGraphSeries;
    private ArrayList<String> stockCloseValueList;

    public DetailFragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_fragment_two, container, false);
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

        graphView = (GraphView) getView().findViewById(R.id.point_graph);
        if(savedInstanceState != null && savedInstanceState.getStringArrayList("stockCloseValueList") != null){
            setDetailGraphView(savedInstanceState.getStringArrayList("stockCloseValueList"));
        }
    }

    public void setDetailGraphView(ArrayList<String> stockCloseValueList){
        this.stockCloseValueList = stockCloseValueList;
        pointsGraphSeries = new PointsGraphSeries();
        for(int i = 0; i<stockCloseValueList.size(); i++){
            pointsGraphSeries.appendData(new DataPoint(i, Double.parseDouble(stockCloseValueList.get(i))), false, 366);
        }

        graphView.addSeries(pointsGraphSeries);

        Viewport viewport = graphView.getViewport();
        viewport.setMinY(0);
        viewport.setMaxY(1000);
        viewport.setScrollable(true);
        viewport.setScalable(true);
    }
}
