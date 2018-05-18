package com.journeyOS.github.ui.fragment.repos.adapter;

import com.journeyOS.base.adapter.BaseAdapterData;
import com.journeyOS.github.R;
import com.journeyOS.github.entity.User;

public class RepositoryData implements BaseAdapterData {

    public String name;

    public String description;

    public int stargazersCount;

    public int forksCount;

    public User owner;

    public String language;

    @Override
    public int getContentViewId() {
        return R.layout.layout_item_repository;
    }

    @Override
    public String toString() {
        return "RepositoryData{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stargazersCount=" + stargazersCount +
                ", forksCount=" + forksCount +
                ", owner=" + owner +
                '}';
    }
}
