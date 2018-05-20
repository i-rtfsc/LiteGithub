package com.journeyOS.github;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.api.userprovider.AuthUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;

public class MaterialDrawer {
    private static final String TAG = MaterialDrawer.class.getSimpleName();
    private Activity mContext;
    AccountHeader headerResult;
    Drawer result;

    public MaterialDrawer(Activity context) {
        mContext = context;
    }

    public Drawer initDrawer(Bundle savedInstanceState, AuthUser user, Toolbar toolbar) {
        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext)
                .load(Uri.parse(user.avatar))
                .placeholder(R.mipmap.user)
                .into(imageView);

        final IProfile profile = new ProfileDrawerItem()
                .withName(user.name)
                .withEmail(user.email)
                .withIcon(imageView.getDrawable());

        headerResult = new AccountHeaderBuilder()
                .withActivity(mContext)
                .withCompactStyle(true)
                .addProfiles(profile,
                        new ProfileSettingDrawerItem().withName(R.string.add_account).withDescription(R.string.add_new_account).withIcon(R.drawable.svg_add_user).withIdentifier(9),
                        new ProfileSettingDrawerItem().withName(R.string.manage_account).withIcon(R.drawable.svg_menu_settings))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        LogUtils.d(TAG, "onProfileChanged() called with: view = [" + view + "], profile = [" + profile + "], current = [" + current + "]");
                        return false;
                    }
                })
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        LogUtils.d(TAG, "onClick() called with: view = [" + view + "], profile = [" + profile + "]");
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(mContext)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult)
                .withDisplayBelowStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.profile).withIcon(R.drawable.svg_menu_user).withTag(Constant.MENU_PROFILE),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.my_repos).withIcon(R.drawable.svg_menu_repos).withTag(Constant.MENU_REPOS),
                        new PrimaryDrawerItem().withIdentifier(3).withName(R.string.notifications).withIcon(R.drawable.svg_menu_notification).withTag(Constant.MENU_NOTIFICATION),
                        new PrimaryDrawerItem().withIdentifier(4).withName(R.string.issues).withIcon(R.drawable.svg_menu_issue).withTag(Constant.MENU_ISSUE),
                        new SectionDrawerItem().withName(R.string.advanced),
                        new SecondaryDrawerItem().withIdentifier(5).withName(R.string.search).withIcon(R.drawable.svg_menu_search).withTag(Constant.MENU_SEARCH),
                        new SecondaryDrawerItem().withIdentifier(6).withName(R.string.starred).withIcon(R.drawable.svg_menu_starred).withTag(Constant.MENU_STARRED)
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withIdentifier(7).withName(R.string.settings).withIcon(R.drawable.svg_menu_settings).withTag(Constant.MENU_SETTINGS),
                        new SecondaryDrawerItem().withIdentifier(8).withName(R.string.about).withIcon(R.drawable.svg_menu_about).withTag(Constant.MENU_ABOUT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        String tag = (String) drawerItem.getTag();
                        listener.handleDrawerItem(tag, false);
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (result != null && result.isDrawerOpen()) {
                            result.closeDrawer();
                        }
                        String tag = (String) drawerItem.getTag();
                        listener.handleDrawerItem(tag, true);
                        return false;
                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        return result;
    }

    private OnDrawerItemClickListener listener;
    public void setOnDrawerItemClickListener(OnDrawerItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnDrawerItemClickListener {
        void handleDrawerItem(String tag, boolean longClick);
    }
}
