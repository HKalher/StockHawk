package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sam_chordas.android.stockhawk.ui.DetailActivity;

/**
 * Created by sam_chordas on 11/9/15.
 */
public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

  @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

  }

  private GestureDetector gestureDetector;
  private OnItemClickListener listener;

  // Interface to communicate between recylerview and activity
  public interface OnItemClickListener{
    void onItemClick(View v, int position);
  }
    // passing OnItemClickListener as a parameter, This function is called from MyStocksActivity
  public RecyclerViewItemClickListener(final Context context, OnItemClickListener listener) {
    this.listener = listener;
    gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
      @Override public boolean onSingleTapUp(MotionEvent e) {
        return true;
      }
    });
  }

  @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
      listener.onItemClick(childView, view.getChildPosition(childView));
      return true;
    }
    return false;
  }

  @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
}