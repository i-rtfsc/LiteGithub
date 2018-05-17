package com.journeyOS.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;


/**
 * implement BaseViewInit interface is convenient to generate views on ViewHolder
 */
public abstract class BaseViewHolder<T extends BaseAdapterData> extends RecyclerView.ViewHolder implements IProvideItemId {

    protected BaseRecyclerAdapter mBaseAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public BaseViewHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView);
        mBaseAdapter = baseRecyclerAdapter;
        ButterKnife.bind(this, itemView);
        setUIContext(itemView.getContext());
    }

    private void setUIContext(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    protected Context getContext() {
        return mContext;
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public abstract void updateItem(T data, int position);
}
