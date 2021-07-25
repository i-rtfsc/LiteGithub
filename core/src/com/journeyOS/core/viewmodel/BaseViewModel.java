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


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.journeyOS.literouter.Router;

public abstract class BaseViewModel extends ViewModel implements LifecycleOwner {

    private LifecycleOwner mLifecycleOwner;

    protected abstract void onCreate();

    <T extends BaseViewModel> T attachLifecycleOwner(LifecycleOwner lifecycleOwner) {
        T currentModel = (T) this;

        if (mLifecycleOwner != null) {
            return currentModel;
        }

        this.mLifecycleOwner = lifecycleOwner;

        Router.getDefault().register(this);
        onCreate();
        return currentModel;
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleOwner.getLifecycle();
    }

    /**
     * called when attached activity or fragment onDestroy called
     */
    @Override
    protected void onCleared() {
        Router.getDefault().unregister(this);
    }
}
