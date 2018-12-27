package com.journeyOS.github.ui.fragment.repos.adapter;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.ImageEngine;
import com.journeyOS.github.R;

import butterknife.BindView;
import co.revely.gradient.RevelyGradient;

public class RepositoryHolder extends BaseViewHolder<RepositoryData> {

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

    private int[] descriptionColors = new int[]{
            getContext().getResources().getColor(R.color.fuchsia),
            getContext().getResources().getColor(R.color.violet)
    };

    public RepositoryHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
        RevelyGradient
                .radial(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f, getContext().getResources().getDisplayMetrics()))
                .colors(descriptionColors)
                .on(tvRepoDescription);
    }

    @Override
    public void updateItem(RepositoryData data, int position) {
        tvRepoName.setText(data.name);
        tvRepoDescription.setText(data.description);
        tvStarNum.setText(String.valueOf(data.stargazersCount));
        tvForkNum.setText(String.valueOf(data.forksCount));
        tvOwnerName.setText(data.owner.login);
        ivLanguage.setText(data.language);
        ImageEngine.load(CoreManager.getContext(), data.owner.avatarUrl, ivUserAvatar, R.mipmap.user);
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_repository;
    }

}
