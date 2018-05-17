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

package com.journeyOS.core.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * 所有的ViewModel在此获取，便于方便管理
 */

public class ModelProvider {


    /**
     * 一个类中，对同一个  {@link BaseViewModel} 应该只调用一次getModel，然后类中做缓存
     *
     * @param fragment  ViewModelProvider 所依赖的fragment
     * @param viewModel 要获取的 BaseViewModel
     * @param <T>       BaseViewModel 的实现
     * @return <T> 的实例
     */
    @MainThread
    public static <T extends BaseViewModel> T getModel(Fragment fragment, Class<T> viewModel) {
        return ViewModelProviders.of(fragment).get(viewModel).attachLifecycleOwner(fragment);
    }

    /**
     * see {@link #getModel}
     */
    @MainThread
    public static <T extends BaseViewModel> T getModel(Context activity, Class<T> viewModel) {

        if (!(activity instanceof FragmentActivity)) {
            throw new RuntimeException("context must a FragmentActivity instance");
        }
        FragmentActivity fragmentActivity = (FragmentActivity) activity;

        return ViewModelProviders.of(fragmentActivity).get(viewModel).attachLifecycleOwner(fragmentActivity);
    }
}
