package com.journeyOS.github.ui.fragment.repos.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.core.CoreManager;
import com.journeyOS.github.R;
import com.journeyOS.github.ui.viewer.RepositoryActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

public class RepositoryHolder extends BaseViewHolder<RepositoryData> {
    RepositoryData mRepositoryData;

    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_repo_name)
    TextView tvRepoName;
    @BindView(R.id.tv_repo_description)
    TextView tvRepoDescription;
    @BindView(R.id.tv_star_num)
    TextView tvStarNum;
    @BindView(R.id.tv_fork_num)
    TextView tvForkNum;
    @BindView(R.id.tv_owner_name)
    TextView tvOwnerName;
    @BindView(R.id.tv_language)
    TextView ivLanguage;

    public RepositoryHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(RepositoryData data, int position) {
        mRepositoryData = data;
        tvRepoName.setText(data.name);
        tvRepoDescription.setText(data.description);
        tvStarNum.setText(String.valueOf(data.stargazersCount));
        tvForkNum.setText(String.valueOf(data.forksCount));
        tvOwnerName.setText(data.owner.login);
        ivLanguage.setText(data.language);
        Picasso.with(CoreManager.getContext())
                .load(data.owner.avatarUrl)
                .placeholder(R.mipmap.user)
                .into(ivUserAvatar);
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_repository;
    }

    @OnClick(R.id.cardView)
    public void onCardViewClick() {
        String login = mRepositoryData.owner.login;
        String name = mRepositoryData.name;
        String defaultBranch = mRepositoryData.defaultBranch;
        RepositoryActivity.show(CoreManager.getContext(), login, name, defaultBranch);
    }
}
