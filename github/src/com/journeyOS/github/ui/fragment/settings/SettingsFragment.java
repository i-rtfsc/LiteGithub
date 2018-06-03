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

package com.journeyOS.github.ui.fragment.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.widget.SettingSwitch;
import com.journeyOS.base.widget.SettingView;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.userprovider.IAuthUserProvider;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.activity.splash.SplashActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.code_theme)
    SettingView codeTheme;
    @BindView(R.id.use_cache)
    SettingSwitch useCache;
    @BindView(R.id.logout)
    SettingView logout;

    static Activity mContext;

    public static Fragment newInstance(Activity activity) {
        SettingsFragment fragment = new SettingsFragment();
        mContext = activity;
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.settings_fragment;
    }

    @Override
    public void initViews() {
        boolean isCache = SpUtils.getInstant().getBoolean(Constant.USE_CACHE, true);
        useCache.setCheck(isCache);
    }

    @OnClick({R.id.code_theme})
    void listenerCodeTheme() {
        showThemeChooser();
    }

    @OnClick({R.id.use_cache})
    void listenerUseCache() {
        boolean isCache = !SpUtils.getInstant().getBoolean(Constant.USE_CACHE, true);
        useCache.setCheck(isCache);
        SpUtils.getInstant().put(Constant.USE_CACHE, isCache);
    }

    @OnClick({R.id.logout})
    void listenerLogout() {
        verifyLogout();
    }

    @OnClick({R.id.image_engine})
    void listenerImageEngine() {
        showImageEngine();
    }

    void showThemeChooser() {
        final String[] items = mContext.getResources().getStringArray(R.array.theme_array);
        int item = SpUtils.getInstant().getBoolean(Constant.THEME, true) ? 0 : 1;
        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.CornersAlertDialog)
                .setTitle(mContext.getString(R.string.code_theme))
                .setSingleChoiceItems(items, item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SpUtils.getInstant().put(Constant.THEME, which == 0);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();

    }

    void verifyLogout() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.CornersAlertDialog)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CoreManager.getImpl(IAuthUserProvider.class).getUserWorkHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                CoreManager.getImpl(IAuthUserProvider.class).deleteAll();
                                SplashActivity.newInstance(mContext);
                            }
                        });
                    }
                })
                .create();
        dialog.show();
    }

    void showImageEngine() {
        final String[] items = mContext.getResources().getStringArray(R.array.image_engine_array);
        int item = SpUtils.getInstant().getString(Constant.IMAGE_ENGINE, Constant.IMAGE_ENGINE_GLIDE).equals(Constant.IMAGE_ENGINE_GLIDE) ? 0 : 1;

        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.CornersAlertDialog)
                .setTitle(mContext.getString(R.string.image_engine))
                .setSingleChoiceItems(items, item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which == 0) {
                            SpUtils.getInstant().put(Constant.IMAGE_ENGINE, Constant.IMAGE_ENGINE_GLIDE);
                        } else if (which == 1) {
                            SpUtils.getInstant().put(Constant.IMAGE_ENGINE, Constant.IMAGE_ENGINE_PICASSO);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();

    }
}
