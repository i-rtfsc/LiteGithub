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

package com.journeyOS.github.ui.fragment.user.adapter;

import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.core.CoreManager;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.activity.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserHolder extends BaseViewHolder<UserData> {
    static final String TAG = UserHolder.class.getSimpleName();
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.name)
    TextView name;

    UserData mUserData;

    public UserHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(UserData data, int position) {
        mUserData = data;
        name.setText(data.login);
        Picasso.with(CoreManager.getContext())
                .load(data.avatarUrl)
                .placeholder(R.mipmap.user)
                .into(avatar);
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_user;
    }

    @OnClick({R.id.cardView})
    void onUserItemClick() {
        LogUtil.d(TAG, "user item has been click");
        //ProfileActivity.show(CoreManager.getContext(), mUserData.login);
    }
}
