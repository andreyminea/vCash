package com.carla.vcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.carla.managers.SharedPrefsSingleton;
import com.carla.vcash.Adapters.PagerAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    private final String PAUSE_SHOW = "INTENT_PAUSE_SHOW";
    private final String TAG = "DEBUGG";
    private CircularProgressIndicator progressIndicator;
    private TabLayout tabLayout;
    private TabItem tabHome;
    private TabItem tabPayments;
    private TabItem tabTransfer;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    /*
    Cont google
    User: carla.licenta2021
    Pass: Licenta@2021
     */
    /*
    Interogare sold
    Alimentare cont
    Retragere din cont
    Istoric tranzactii
    (o aplicatie destinata transferurilor dintre conturi, fara numerar)
    Credentialele pt logare sunt nr cardului si pinul
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressIndicator = (CircularProgressIndicator) findViewById(R.id.progressIndicator);
        tabLayout = (TabLayout) findViewById(R.id.toolbar);
        tabHome = (TabItem) findViewById(R.id.tabHome);
        tabPayments = (TabItem) findViewById(R.id.tabPayments);
        tabTransfer = (TabItem) findViewById(R.id.tabTransfer);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(pagerAdapter);


        boolean showPause = getIntent().getBooleanExtra(PAUSE_SHOW, true);
        Log.d(TAG, Boolean.toString(showPause));
        pauseApp(showPause);
    }

    private void pauseApp(boolean execute)
    {
        if(!execute)
        {
            progressIndicator.setVisibility(View.INVISIBLE);
            return;
        }
        // new thread is necessary so the main thread don't freeze to much causing a shutdown
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfUserSigned();
            }
        }, 1000); // for circle progress to be seen
    }

    private void checkIfUserSigned()
    {
        SharedPreferences sharedPrefs = SharedPrefsSingleton.getPrefs(getApplicationContext());
        if(!sharedPrefs.getBoolean(SharedPrefsSingleton.SP_HASACCOUNT, false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        if(!getIntent().getBooleanExtra("OK", false)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}