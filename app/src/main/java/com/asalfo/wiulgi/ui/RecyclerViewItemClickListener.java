package com.asalfo.wiulgi.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

  @NonNull
  private final GestureDetector gestureDetector;
  private final OnItemClickListener listener;
  public RecyclerViewItemClickListener(Context context, OnItemClickListener listener) {
    this.listener = listener;
    gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
      @Override
      public boolean onSingleTapUp(MotionEvent e) {
        return true;
      }
    });
  }

  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

  }

  @Override
  public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
      listener.onItemClick(childView, view.getChildPosition(childView));
      return true;
    }
    return false;
  }

  @Override
  public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

  public interface OnItemClickListener{
    void onItemClick(View v, int position);
  }
}
