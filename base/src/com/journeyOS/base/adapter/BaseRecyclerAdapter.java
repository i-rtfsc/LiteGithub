package com.journeyOS.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();
    protected HolderClickListener mHolderClickListener;
    private Context mContext;
    private SparseArray<Class<? extends BaseViewHolder>> typeHolders = new SparseArray();
    private List<BaseAdapterData> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void registerHolder(Class<? extends BaseViewHolder> viewHolder, int itemViewType) {
        typeHolders.put(itemViewType, viewHolder);
    }

    public <T extends BaseAdapterData> void registerHolder(Class<? extends BaseViewHolder> viewHolder, T data) {
        if (data == null) {
            return;
        }
        typeHolders.put(data.getContentViewId(), viewHolder);
        addData(data);
    }

    public void registerHolder(Class<? extends BaseViewHolder> viewHolder, List<? extends BaseAdapterData> data) {
        if (BaseUtils.isEmpty(data)) {
            return;
        }
        typeHolders.put(data.get(0).getContentViewId(), viewHolder);
        addData(data);
    }

    public void clear() {
        mData.clear();
    }

    public void addData(List<? extends BaseAdapterData> data) {
        if (data == null) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public <T extends BaseAdapterData> void addData(T data) {
        if (data == null) {
            return;
        }
        mData.add(data);

        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(viewType, parent, false);
        BaseViewHolder viewHolder = new NoDataViewHolder(itemView, this);
        try {
            Class<?> cls = typeHolders.get(viewType);
            Constructor holderConstructor = cls.getDeclaredConstructor(View.class, BaseRecyclerAdapter.class);
            holderConstructor.setAccessible(true);
            viewHolder = (BaseViewHolder) holderConstructor.newInstance(itemView, this);
        } catch (NoSuchMethodException e) {
            LogUtils.d(TAG, " create view holder error = " + e);
        } catch (Exception e) {
            LogUtils.d(TAG, " create view holder error = " + e);
        }
        return viewHolder;
    }

    public List<BaseAdapterData> getData() {
        return mData;
    }

    public void setData(List<? extends BaseAdapterData> data) {
        if (BaseUtils.isEmpty(data)) {
            return;
        }
        mData.clear();
        addData(data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (BaseUtils.isEmpty(mData) || BaseUtils.isNull(mData.get(position))) {
            return;
        }

        if (getItemViewType(position) != holder.getContentViewId()) {
            return;
        }

        holder.updateItem(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getContentViewId();
    }

    public void setOnHolderClickListener(HolderClickListener clickListener) {
        this.mHolderClickListener = clickListener;
    }

    public <T extends BaseAdapterData> void onHolderClicked(int position, T data) {
        if (mHolderClickListener != null) {
            mHolderClickListener.onHolderClicked(position, data);
        }
    }

    public interface HolderClickListener<T extends BaseAdapterData> {
        void onHolderClicked(int position, T data);
    }
}
