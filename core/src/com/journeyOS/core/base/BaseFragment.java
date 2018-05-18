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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.journeyOS.core.R;
import com.journeyOS.literouter.Router;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

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
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }else{
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must show dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content){
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
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_tip))
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
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
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
