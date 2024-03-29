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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyOS.base.utils.ToastyUtils;
import com.journeyOS.core.R;
import com.journeyOS.literouter.Router;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements BaseViewInit, IBaseView {

    protected FragmentActivity mActivity;
    private ProgressDialog mProgressDialog;

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
        initViews();
        initDataObserver(savedInstanceState);
        return rootView;
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
    public void showToast(ToastyUtils.ToastType toastType, String message, boolean isLong) {
        ToastyUtils.showToast(getActivity(), toastType, message, isLong);
    }

    @Override
    public void showToast(ToastyUtils.ToastType toastType, int resourceId, boolean isLong) {
        ToastyUtils.showToast(getActivity(), toastType, resourceId, isLong);
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

}
