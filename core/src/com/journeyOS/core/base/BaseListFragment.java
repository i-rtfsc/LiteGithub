/*
 * Copyright (c) 2018 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyOS.core.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.ViewUtils;
import com.journeyOS.core.BuildConfig;
import com.journeyOS.core.R;
import com.journeyOS.core.R2;
import com.journeyOS.literouter.Router;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public abstract class BaseListFragment extends Fragment implements BaseViewInit, IBaseView, IBaseListView, OnRefreshListener {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = BaseListFragment.class.getSimpleName();
    protected FragmentActivity mActivity;
    private ProgressDialog mProgressDialog;

    @BindView(R2.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R2.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerAdapter mAdapter;

    private int curPage = 1;
    private final int DEFAULT_PAGE_SIZE = 30;
    private boolean isLoading = false;
    private boolean canLoadMore = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Router.getDefault().register(this);
        initBeforeView();
    }

    @Override
    public void initBeforeView() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Router.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(attachLayoutRes(), container, false);
        ButterKnife.bind(this, rootView);
        initBaseView();
        initViews();
        initDataObserver(savedInstanceState);
        return rootView;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        onReLoadData();
    }

    void initBaseView() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(getActivity()));

        mAdapter = new BaseRecyclerAdapter(getActivity());
        mAdapter.registerHolder(attachViewHolder(), attachViewHolderRes());
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int itemCount = mAdapter.getItemCount();
                if (itemCount == 0) {
                    mRefreshLayout.setVisibility(View.GONE);
                } else {
                    mRefreshLayout.setVisibility(View.VISIBLE);
                    canLoadMore = itemCount % getPagerSize() == 0;
                    curPage = itemCount % getPagerSize() == 0 ?
                            itemCount / getPagerSize() : (itemCount / getPagerSize()) + 1;
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new ScrollListener());
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.list_fragment;
    }

    protected void initDataObserver(Bundle savedInstanceState) {
    }

    @Override
    public void showProgressDialog(String content) {
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        } else {
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must show dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showShortToast(ToastType toastType, String message) {
        switch (toastType) {
            case NORMAL:
                Toasty.normal(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case INFO:
                Toasty.info(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case WARNING:
                Toasty.warning(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                Toasty.success(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showLongToast(ToastType toastType, String message) {
        switch (toastType) {
            case NORMAL:
                Toasty.normal(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
            case INFO:
                Toasty.info(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
            case WARNING:
                Toasty.warning(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
            case ERROR:
                Toasty.error(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                Toasty.success(getActivity(), message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void showTipDialog(String content) {
        new AlertDialog.Builder(getActivity(), R.style.CornersAlertDialog)
                .setTitle(getString(R.string.dialog_tip))
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        new AlertDialog.Builder(getActivity(), R.style.CornersAlertDialog)
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public int getCurPage() {
        return curPage;
    }

    @Override
    public int getPagerSize() {
        return DEFAULT_PAGE_SIZE;
    }

    @Override
    public void showLoading() {
        isLoading = true;
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isLoading = false;
        mRefreshLayout.setRefreshing(false);
    }

    class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!canLoadMore || isLoading) return;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //only LinearLayoutManager can find last visible
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                if (DEBUG)
                    LogUtils.d(TAG, "RecyclerView scrolling, adapter position of the last visible view = " + lastPosition);
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    onLoadMore(curPage + 1);
                }
            }
        }
    }

}
