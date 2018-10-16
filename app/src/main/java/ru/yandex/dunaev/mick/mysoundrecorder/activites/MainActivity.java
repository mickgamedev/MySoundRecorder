package ru.yandex.dunaev.mick.mysoundrecorder.activites;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.controllers.MyRecordController;
import ru.yandex.dunaev.mick.mysoundrecorder.fragments.RecordFragment;
import ru.yandex.dunaev.mick.mysoundrecorder.fragments.RecordListFragment;
import ru.yandex.dunaev.mick.mysoundrecorder.helpers.CheckPermissionHelper;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;

    private RecordFragment mRecordFragment = new RecordFragment();
    private RecordListFragment mRecordListFragment = new RecordListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecordFragment.setFileObserver(mRecordListFragment.getiMyFileObserver());

        setSupportActionBar(mToolbar);
        setPagerAdapter();

        mTabLayout.setupWithViewPager(mPager);
    }

    private String getPageTitle(int i){
        switch (i){
            case 0:
                return getResources().getString(R.string.page_record);
            case 1:
                return getResources().getString(R.string.page_record_list);
        }
        return "";
    }

    private void setPagerAdapter(){
        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0:
                        return mRecordFragment;
                    case 1:
                        return mRecordListFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return MainActivity.this.getPageTitle(position);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CheckPermissionHelper.REQUEST_PERMISION_CODE) CheckPermissionHelper.onPermissionGranted(this,permissions);
        if(requestCode == CheckPermissionHelper.REQUEST_PERMISION_CODE_DATA) CheckPermissionHelper.onPermissionGrantedData(this,permissions);
    }
}
