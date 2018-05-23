/*
 *
 *  * Copyright (c) 2018 anqi.huang@outlook.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.journeyOS.github.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;

import java.util.ArrayList;
import java.util.List;

public class MainPageAdapter extends FragmentStatePagerAdapter {
    private final List<Pair<BaseFragment, Integer>> mFragmentList = new ArrayList<>();

    private Context context;

    private Fragment curFragment;

    public MainPageAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position).first;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Pair<BaseFragment, Integer> fragmentPair) {
        mFragmentList.add(fragmentPair);
    }

    public View getTabView(int position, ViewGroup parent) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.tab_view, parent, false);
        TextView textView = view.findViewById(R.id.tab_text);
        textView.setText(mFragmentList.get(position).second);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (curFragment.equals(object)) {
            curFragment = null;
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        curFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurFragment() {
        return curFragment;
    }
}
