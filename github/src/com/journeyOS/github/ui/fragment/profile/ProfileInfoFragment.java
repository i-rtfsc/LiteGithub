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

package com.journeyOS.github.ui.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;
import com.journeyOS.github.type.FragmentType;
import com.journeyOS.github.type.RepoType;
import com.journeyOS.github.type.UserType;
import com.journeyOS.github.ui.activity.ContainerActivity;
import com.journeyOS.github.ui.fragment.repos.ReposFragment;
import com.journeyOS.github.ui.fragment.user.UserFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileInfoFragment extends BaseFragment {

    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.joined_time)
    TextView joinedTime;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.link)
    TextView link;
    @BindView(R.id.followers_num_text)
    TextView followersNumText;
    @BindView(R.id.following_num_text)
    TextView followingNumText;
    @BindView(R.id.repos_num_text)
    TextView reposNumText;
    @BindView(R.id.gists_num_text)
    TextView gistsNumText;

    static final String EXTRA_USER = "user";
    User mUser;

    Context mContext;

    public static Fragment newInstance(User user) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USER, user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.profile_info_fragment;
    }

    @Override
    public void initViews() {
        mUser = getArguments().getParcelable(EXTRA_USER);
        initAllViews(mUser);
    }

    void initAllViews(User user) {
        Picasso.with(getActivity())
                .load(user.avatarUrl)
                .into(avatar);
        name.setText(BaseUtils.isBlank(user.name) ? user.login : user.name);
        joinedTime.setText(getString(R.string.joined_at).concat(" ")
                .concat(BaseUtils.getDateStr(user.createdAt)));
        if (BaseUtils.isNull(user.localtion)) {
            location.setVisibility(View.GONE);
        } else {
            location.setText(user.localtion);
        }
        followersNumText.setText(String.valueOf(user.followers));
        followingNumText.setText(String.valueOf(user.following));
        reposNumText.setText(String.valueOf(user.publicRepos));
        gistsNumText.setText(String.valueOf(user.publicGists));
        email.setText(user.email);
        link.setText(user.blog);
    }


    @OnClick({R.id.followers_lay, R.id.following_lay, R.id.repos_lay, R.id.gists_lay,
            R.id.email, R.id.link})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.followers_lay:
                ContainerActivity.showUser(mContext, FragmentType.USER, UserType.FOLLOWERS, mUser.login, mUser.name);
                break;
            case R.id.following_lay:
                ContainerActivity.showUser(mContext, FragmentType.USER, UserType.FOLLOWING, mUser.login, mUser.name);
                break;
            case R.id.repos_lay:
                ContainerActivity.show(mContext, FragmentType.REPOS, RepoType.OWNED);
                break;
            case R.id.gists_lay:
                break;
            case R.id.email:
                break;
            case R.id.link:
                break;
        }
    }

}
