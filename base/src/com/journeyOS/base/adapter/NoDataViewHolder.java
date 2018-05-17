package com.journeyOS.base.adapter;

import android.view.View;


public class NoDataViewHolder extends BaseViewHolder {
    public NoDataViewHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(BaseAdapterData Data, int position) {

    }

    @Override
    public int getContentViewId() {
        return 0;
    }

}
