package com.journeyOS.plugins.about;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.widget.SettingText;
import com.journeyOS.base.widget.SettingView;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.Version;
import com.journeyOS.core.api.repository.IRepositoryProvider;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.plugins.R;
import com.journeyOS.plugins.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutFragment extends BaseFragment {

    @BindView(R2.id.version)
    SettingView mVersion;
    @BindView(R2.id.email)
    SettingView mEmail;
    @BindView(R2.id.github)
    SettingView mGithub;

    static Activity mContext;

    public static Fragment newInstance(Activity activity) {
        AboutFragment fragment = new AboutFragment();
        mContext = activity;
        return fragment;
    }

    @Override
    public int attachLayoutRes() {
        return R.layout.about_fragment;
    }

    @Override
    public void initViews() {
        mVersion.setSummary(Version.getVersionName(mContext));
        mEmail.setSummary(Constant.EMAIL);
        mGithub.setSummary(Constant.GIT_HUB_WEBSITE);
    }

    @OnClick({R2.id.version})
    void listenerVersion() {
        BaseUtils.openInMarket(mContext);
    }

    @OnClick({R2.id.email})
    void listenerEmail() {
        BaseUtils.launchEmail(mContext, Constant.EMAIL);
    }

    @OnClick({R2.id.github})
    void listenerGithub() {
        CoreManager.getImpl(IRepositoryProvider.class).navigationRepositoryActivity(mContext, Constant.GIT_HUB_ID, Constant.LITE_GIT_HUB);
    }
}
