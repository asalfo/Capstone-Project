package com.asalfo.wiulgi.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.asalfo.wiulgi.data.provider.WiulgiContract;


public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
  private static final String LOG_TAG = CursorRecyclerViewAdapter.class.getSimpleName();
  @Nullable
  private Cursor mCursor;
  private boolean dataIsValid;
  private int rowIdColumn;
  @NonNull
  private final DataSetObserver mDataSetObserver;
  CursorRecyclerViewAdapter(Context context, @Nullable Cursor cursor){
    mCursor = cursor;
    dataIsValid = cursor != null;
    rowIdColumn = dataIsValid ? mCursor.getColumnIndex(WiulgiContract.ItemsColumns._ID) : -1;
    mDataSetObserver = new NotifyingDataSetObserver();
    if (dataIsValid){
      mCursor.registerDataSetObserver(mDataSetObserver);
    }
  }

  @Nullable
  Cursor getCursor(){
    return mCursor;
  }

  @Override
  public int getItemCount(){
    if (dataIsValid && mCursor != null){
      return mCursor.getCount();
    }
    return 0;
  }

  @Override
  public long getItemId(int position) {
    if (dataIsValid && mCursor != null && mCursor.moveToPosition(position)){
      return mCursor.getLong(rowIdColumn);
    }
    return 0;
  }

  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }

  protected abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

  @Override
  public void onBindViewHolder(VH viewHolder, int position) {
    if (!dataIsValid){
      throw new IllegalStateException("This should only be called when Cursor is valid");
    }
    if (!mCursor.moveToPosition(position)){
      throw new IllegalStateException("Could not move Cursor to position: " + position);
    }

    onBindViewHolder(viewHolder, mCursor);
  }

  @Nullable
  public Cursor swapCursor(@NonNull Cursor newCursor){
    if (newCursor == mCursor){
      return null;
    }
    final Cursor oldCursor = mCursor;
    if (oldCursor != null && mDataSetObserver != null){
      oldCursor.unregisterDataSetObserver(mDataSetObserver);
    }
    mCursor = newCursor;
    if (mCursor != null){
      if (mDataSetObserver != null){
        mCursor.registerDataSetObserver(mDataSetObserver);
      }
      rowIdColumn = newCursor.getColumnIndexOrThrow(WiulgiContract.ItemsColumns._ID);
      dataIsValid = true;
      notifyDataSetChanged();
    }else{
      rowIdColumn = -1;
      dataIsValid = false;
      notifyDataSetChanged();
    }
    return oldCursor;
  }

  private class NotifyingDataSetObserver extends DataSetObserver {
    @Override
    public void onChanged() {
      super.onChanged();
      dataIsValid = true;
      notifyDataSetChanged();
    }

    @Override
    public void onInvalidated() {
      super.onInvalidated();
      dataIsValid = false;
      notifyDataSetChanged();
    }
  }
}
