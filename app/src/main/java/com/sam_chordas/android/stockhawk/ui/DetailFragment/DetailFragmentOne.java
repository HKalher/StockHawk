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
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragmentOne extends Fragment{

    private GraphView graphView;
    private LineGraphSeries lineGraphSeries;
    private ArrayList<String> stockCloseValueList;

    public DetailFragmentOne() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_fragment_one, container, false);
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

        graphView = (GraphView) getView().findViewById(R.id.line_graph);
        if(savedInstanceState != null && savedInstanceState.getStringArrayList("stockCloseValueList") != null){
            setDetailGraphView(savedInstanceState.getStringArrayList("stockCloseValueList"));
        }
        Toast.makeText(getContext(), getString(R.string.data_fetching_message), Toast.LENGTH_LONG);
    }

    public void setDetailGraphView(ArrayList<String> stockCloseValueList){
        this.stockCloseValueList = stockCloseValueList;
        lineGraphSeries = new LineGraphSeries();
        for(int i = 0; i<stockCloseValueList.size(); i++){
            lineGraphSeries.appendData(new DataPoint(i, Double.parseDouble(stockCloseValueList.get(i))), false, 366);
        }

        graphView.addSeries(lineGraphSeries);

        Viewport viewport = graphView.getViewport();
        viewport.setMinY(0);
        viewport.setMaxY(1000);
        viewport.setScrollable(true);
        viewport.setScalable(true);
    }
}
